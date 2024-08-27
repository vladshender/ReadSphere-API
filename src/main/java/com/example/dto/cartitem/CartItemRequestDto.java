package com.example.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotBlank
    private Long bookId;

    @Min(value = 1)
    private int quantity;
}
