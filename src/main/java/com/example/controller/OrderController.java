package com.example.controller;

import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderUpdateStatusRequestDto;
import com.example.dto.orderitem.OrderItemResponseDto;
import com.example.model.User;
import com.example.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for management orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Сreating an order for a logged-in user")
    @PostMapping
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrderByUser(user, requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Get all orders of the logged in user")
    @GetMapping
    public List<OrderResponseDto> getOrdersByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderByUser(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update order status by id")
    @PatchMapping("/{id}")
    public OrderResponseDto exchangeStatus(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateStatusRequestDto requestDto
    ) {
        return orderService.updateStatusByOrder(id, requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Get all items for order by order id")
    @GetMapping("/{id}/items")
    public List<OrderItemResponseDto> getAllItemsByOrderId(@PathVariable Long id) {
        return orderService.getOrderItemsByOrderId(id);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Get item for the order by id and product id")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getItemFromOrderById(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getOrderItemFromOrderById(orderId, itemId);
    }
}
