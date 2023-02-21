package com.microservice.order.service.dto;

import com.microservice.order.service.model.OrderLineItems;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.OneToMany;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderRequestDto {
    private String orderNumber;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
