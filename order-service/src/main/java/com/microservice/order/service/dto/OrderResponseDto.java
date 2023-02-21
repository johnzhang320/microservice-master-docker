package com.microservice.order.service.dto;

import com.microservice.order.service.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private String orderNumber;

    private List<OrderLineItems> orderLineItemList;
}
