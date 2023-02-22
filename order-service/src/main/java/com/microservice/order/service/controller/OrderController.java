package com.microservice.order.service.controller;

import com.microservice.order.service.dto.OrderRequestDto;
import com.microservice.order.service.dto.OrderResponseDto;
import com.microservice.order.service.exception.OrderException;
import com.microservice.order.service.model.Order;
import com.microservice.order.service.repository.OrderRepository;
import com.microservice.order.service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    /**
     * POST
      http://localhost:8000/orders/placeOrder
      post body , only need productId and quantity , other data fetch from inventory

      {
          "orderLineItemsDtoList": [
              {

                 "productId": "63e2b10db5176c6409fcb611",
                 "quantity" : 245,
                 "price": 275.5
              }
         ]
       }
     * @param orderRequestDto
     * @return
     */
/*
    @PostMapping("/placeOrderTimeout")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    //@Retry(name ="inventory")
    public CompletableFuture<String> placeOrderTimeout(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("OrderRequestDto:"+orderRequestDto.toString());
        Order order = new Order();
        return CompletableFuture.supplyAsync(()->orderService.placeOrderTimeout(orderRequestDto));
    }
    public CompletableFuture<String> fallbackMethod(OrderRequestDto orderRequestDto, OrderException ex) {
        return CompletableFuture.supplyAsync(()->"Oops, Something went wrog , please wait for a moment1");
    }*/

    @PostMapping("/placeOrderTimeout")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    @Retry(name ="inventory")
    public ResponseEntity placeOrderTimeout(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("OrderRequestDto:"+orderRequestDto.toString());
        Order order = orderService.placeOrder(orderRequestDto, true);
        return ResponseEntity.ok(order);
    }
    public String fallbackMethod(OrderRequestDto orderRequestDto, OrderException ex) {
        return "Oops, Something went wrong , please wait for a moment !";
    }

    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("OrderRequestDto:"+orderRequestDto.toString());
        Order order =orderService.placeOrder(orderRequestDto,false);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {

        return orderService.getALLOrders();
    }

}
