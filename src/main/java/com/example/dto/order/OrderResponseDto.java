package com.example.dto.order;

import com.example.dto.orderitem.OrderItemResponseDto;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItemsList;
    private String orderDate;
    private BigDecimal total;
    private String status;
}
