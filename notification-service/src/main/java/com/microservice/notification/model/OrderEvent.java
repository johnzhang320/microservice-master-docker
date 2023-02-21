package com.microservice.notification.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderEvent {
    private String orderNumber;

}
