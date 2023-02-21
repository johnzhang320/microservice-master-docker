package com.microservice.order.service.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="t_order_line_items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String skuCode;
     @NotNull
    private String productId;

    private String productName;
    @NotNull
    private BigDecimal price;

    private String description;

    private String inventoryStatus;

    private Integer quantity;
}
