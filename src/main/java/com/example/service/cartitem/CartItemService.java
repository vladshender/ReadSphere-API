package com.example.service.cartitem;

import com.example.dto.cartitem.CartItemDto;
import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.model.ShoppingCart;

public interface CartItemService {
    void save(CartItemRequestDto requestDto);

    CartItemDto updateById(Long id, CartItemUpdateDto updateDto);

    void deleteById(Long id);

    ShoppingCart getShoppingCart();
}
