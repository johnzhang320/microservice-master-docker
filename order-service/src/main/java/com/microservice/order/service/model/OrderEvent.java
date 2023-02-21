package com.microservice.order.service.model;

import com.sun.istack.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderEvent {
    private String orderNumber;
    private String skuCode;

    private String productId;

    private String productName;

    private BigDecimal price;

    private String description;

    private String inventoryStatus;

    private Integer quantity;

}
