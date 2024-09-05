package com.example.dto.order;

import com.example.model.Order;
import jakarta.validation.constraints.NotNull;

public record OrderUpdateStatusRequestDto(@NotNull Order.Status status) {
}
