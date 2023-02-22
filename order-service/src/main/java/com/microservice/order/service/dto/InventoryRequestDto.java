package com.microservice.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequestDto {
    // product name

    private String productName;
    private Integer quantity;

    private String productId;

}
