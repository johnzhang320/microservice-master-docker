package com.microservice.order.service.service;

import com.microservice.order.service.config.WebConfig;
import com.microservice.order.service.dto.InventoryResponseDto;
import com.microservice.order.service.dto.OrderLineItemsDto;
import com.microservice.order.service.dto.OrderRequestDto;
import com.microservice.order.service.dto.OrderResponseDto;
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

    private final WebClient webClient;

    private final InventoryProxy inventoryProxy;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String PLACE_ORDER_TOPIC="place-order-topic";
    public String placeOrder(OrderRequestDto orderRequestDto, Order order, boolean timeout) {

        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList =orderRequestDto.getOrderLineItemsDtoList().stream()
                .map(orderLineItemsDto -> {
                    // determine if we can place this item, services communication by feign
                    InventoryResponseDto inventoryResponseDto =
                            timeout ? inventoryProxy.findProdFromInventorydbTimeout(orderLineItemsDto.getProductId())
                                    : inventoryProxy.findProdFromInventorydb(orderLineItemsDto.getProductId());
                    if (inventoryResponseDto ==null ){
                        orderLineItemsDto.setInventoryStatus("There is no item "+inventoryResponseDto.getProductName() +" in inventory ");
                        orderLineItemsDto.setQuantity(0);
                    } else {
                        if (inventoryResponseDto.getQuantity() < orderLineItemsDto.getQuantity()) {
                            orderLineItemsDto.setInventoryStatus("Quantity of requested item  "+orderLineItemsDto.getProductName() +" is not enough in inventory ");
                            orderLineItemsDto.setQuantity(0);
                        }
                    }
                    Integer orderQuantity = orderLineItemsDto.getQuantity();
                    // queue will to do such subtract in inventory ,
                   Integer inventoryRemainQuantity= inventoryResponseDto.getQuantity() - orderLineItemsDto.getQuantity() ;
                    // inventory product information will be written to orderLineItemsDto
                    OrderLineItems orderLineItems = OrderLineItems.builder()
                            .productId(orderLineItemsDto.getProductId())
                            .skuCode(inventoryResponseDto.getSkuCode())
                            .productName(inventoryResponseDto.getProductName())
                            .description(inventoryResponseDto.getDescription())
                            .inventoryStatus("shipping")
                            .price(orderLineItemsDto.getPrice())
                            .quantity(orderLineItemsDto.getQuantity())
                            .build(); //modelMapper.map(orderLineItemsDto,OrderLineItems.class);
                    log.info(orderLineItems.toString());
                    log.info(inventoryResponseDto.toString());
                    return orderLineItems;

                }).collect(Collectors.toList());
                order.setOrderLineItemList(orderLineItemsList);
                order = orderRepository.save(order);

                // send notice to notification-service

                String orderNumber = order.getOrderNumber();
                order.getOrderLineItemList().forEach(v -> {
                    OrderEvent orderEvent = OrderEvent.builder()
                            .orderNumber(orderNumber)
                            .productName(v.getProductName())
                            .description(v.getDescription())
                            .price(v.getPrice())
                            .productId(v.getProductId())
                            .quantity(v.getQuantity())
                            .inventoryStatus(v.getInventoryStatus())
                            .skuCode(v.getSkuCode())
                            .build();
                    kafkaTemplate.send(PLACE_ORDER_TOPIC,orderEvent);
        });
        log.info("successfully place order !");
        return "successfully place order !";

    }


    public List<OrderResponseDto> getALLOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(
                order -> {
                    return modelMapper.map(order, OrderResponseDto.class);
                }
        ).collect(Collectors.toList());
    }
}
