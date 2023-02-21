package com.microservice.inventory.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponseDto {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}

