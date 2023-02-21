package com.microservice.notification.service;

import com.microservice.notification.model.Order;
import com.microservice.notification.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlaceOrderListener {
    private static final String PLACE_ORDER_TOPIC="place-order-topic";
    @KafkaListener(topics=PLACE_ORDER_TOPIC, groupId="orderGroup")
    public void kafkaListener(OrderEvent orderEvent) {
        log.info("Received an order: {}",orderEvent.getOrderNumber());
    }
}
