package com.example.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotEmpty
    private Long bookId;

    @Min(value = 1)
    private int quantity;
}
