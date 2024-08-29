package com.example.dto.shoppingcart;

import com.example.dto.cartitem.CartItemDto;
import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItemsList;
}
