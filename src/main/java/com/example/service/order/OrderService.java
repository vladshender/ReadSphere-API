package com.example.service.order;

import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderUpdateStatusRequestDto;
import com.example.dto.orderitem.OrderItemResponseDto;
import com.example.model.User;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrderByUser(User user, OrderRequestDto requestDto);

    List<OrderResponseDto> getOrderByUser(User user);

    OrderResponseDto updateStatusByOrder(Long id, OrderUpdateStatusRequestDto responseDto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(User user, Long id);

    OrderItemResponseDto getOrderItemFromOrderById(Long orderId, Long orderItemsId);
}
