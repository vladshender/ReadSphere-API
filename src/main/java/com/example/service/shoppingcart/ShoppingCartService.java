package com.example.service.shoppingcart;

import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto save(User user, CartItemRequestDto requestDto);

    ShoppingCartResponseDto updateById(User user, Long id, CartItemUpdateDto updateDto);

    void deleteById(Long id);

    ShoppingCartResponseDto getShoppingCart(User user);

    void createShoppingCart(User user);

}
