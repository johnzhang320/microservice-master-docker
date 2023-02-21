package com.microservice.order.service.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderLineItemsDto {
    private String skuCode;
    @NotNull
    private String productId;

    private String productName;
    private BigDecimal price;

    private String description;
    @NotNull
    private Integer quantity;

    private String inventoryStatus;
}
