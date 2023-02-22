package com.microservice.order.service.service;


import com.microservice.order.service.dto.InventoryResponseDto;
import com.microservice.order.service.dto.OrderLineItemsDto;
import com.microservice.order.service.dto.OrderRequestDto;
import com.microservice.order.service.dto.OrderResponseDto;
import com.microservice.order.service.exception.OrderException;
import com.microservice.order.service.feignclient.InventoryProxy;
import com.microservice.order.service.model.Order;
import com.microservice.order.service.model.OrderEvent;
import com.microservice.order.service.model.OrderLineItems;
import com.microservice.order.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final ModelMapper modelMapper;

    private final OrderRepository orderRepository;

    private final InventoryProxy inventoryProxy;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String PLACE_ORDER_TOPIC="place-order-topic";
    public Order placeOrder(OrderRequestDto orderRequestDto, boolean timeout) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList =orderRequestDto.getOrderLineItemsDtoList().stream()
                .map(orderLineItemsDto -> {
                    // determine if we can place this item, services communication by feign
                    if (orderLineItemsDto.getProductId()==null) {
                        throw new OrderException("There is no requested productId !");
                    }
                    if (orderLineItemsDto.getPrice()==null) {
                        throw new OrderException("There is no requested price !");
                    }
                    if (orderLineItemsDto.getQuantity()==null || orderLineItemsDto.getQuantity()==0) {
                        throw new OrderException("There is no requested quantity !");
                    }
                    InventoryResponseDto inventoryResponseDto = !timeout ?
                            inventoryProxy.findProdFromInventorydb(orderLineItemsDto.getProductId()):
                            inventoryProxy.findProdFromInventorydbTimeout(orderLineItemsDto.getProductId());

                    if (inventoryResponseDto.getProductName() ==null ){
                        throw new OrderException("There is no item: "+inventoryResponseDto.getProductName() +" in inventory ");
                    } else {
                        if (inventoryResponseDto.getQuantity()==0 || inventoryResponseDto.getQuantity()==null) {
                            throw new OrderException("Quantity of requested item: "+inventoryResponseDto.getProductName() +" is zero in inventory ");
                        }
                        if (inventoryResponseDto.getQuantity() < orderLineItemsDto.getQuantity()) {
                            throw new OrderException("Quantity of requested item: "+inventoryResponseDto.getProductName() +"  is not enough in inventory !");
                        }
                    }
                    Integer orderQuantity = orderLineItemsDto.getQuantity();
                    // queue will to do such subtract in inventory ,
                    Integer inventoryRemainQuantity= inventoryResponseDto.getQuantity() - orderLineItemsDto.getQuantity();

                    Long inventoryId =inventoryResponseDto.getId();

                    // reduce the quantity from inventory
                    // second parameter of inventoryProxy.updateInventory() is @RequestParam("quantity")
                    // inventoryProxy.updateInventory(inventoryId,inventoryRemainQuantity);

                    // inventory product information will be written to orderLineItemsDto
                    OrderLineItems orderLineItems = builderOrderLineItems(orderLineItemsDto,inventoryResponseDto);

                    return orderLineItems;
                }).collect(Collectors.toList());

        // set requested order to be saved finally
        order.setOrderLineItemList(orderLineItemsList);
        // save order to order database
        order =  orderRepository.save(order);
        // send order event object to notification-service by KAFKA
        sendOrderEvent(order);

        return order;
    }


    public List<OrderResponseDto> getALLOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(
                order -> {
                    return modelMapper.map(order, OrderResponseDto.class);
                }
        ).collect(Collectors.toList());
    }

    private OrderLineItems builderOrderLineItems(OrderLineItemsDto orderLineItemsDto,InventoryResponseDto inventoryResponseDto) {
        return   OrderLineItems.builder()
                .productId(orderLineItemsDto.getProductId())
                .skuCode(inventoryResponseDto.getSkuCode())
                .productName(inventoryResponseDto.getProductName())
                .description(inventoryResponseDto.getDescription())
                .inventoryStatus("shipping")
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build(); //modelMapper.map(orderLineItemsDto,OrderLineItems.class);
    }

    private void sendOrderEvent(Order order) {
        String orderNumber = order.getOrderNumber();
        order.getOrderLineItemList().forEach(o->{
            OrderEvent orderEvent = OrderEvent.builder()
                    .orderNumber(orderNumber)
                    .productName(o.getProductName())
                    .description(o.getDescription())
                    .quantity(o.getQuantity())
                    .price(o.getPrice())
                    .productId(o.getProductId())
                    .skuCode(o.getSkuCode())
                    .inventoryStatus(o.getInventoryStatus())
                    .build();
            // send notice to Notification-Service
            kafkaTemplate.send(PLACE_ORDER_TOPIC,orderEvent);
        });
    }
}
