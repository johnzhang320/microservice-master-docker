package com.microservice.order.service.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderLineItemsDto {
    private String skuCode;
    @NotBlank(message="ProductId is required")
    private String productId;

    private String productName;

    @NotBlank(message="Price is required")
    private BigDecimal price;

    private String description;
    @NotBlank(message="Quantity is required")
    private Integer quantity;

    private String inventoryStatus;
}
