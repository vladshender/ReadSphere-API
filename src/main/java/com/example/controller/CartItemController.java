package com.example.controller;

import com.example.dto.cartitem.CartItemDto;
import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.service.cartitem.CartItemService;
import com.example.service.shoppingcart.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartItemController {
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public CartItemDto addCartItem(@RequestBody @Valid CartItemRequestDto requestDto) {
        return cartItemService.save(requestDto);
    }

    @GetMapping
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.findAll();
    }

    @PutMapping("/items/{id}")
    public CartItemDto updateCartItem(@PathVariable Long id,
                                      @RequestBody CartItemUpdateDto updateDto) {
        return cartItemService.updateById(id, updateDto);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteById(id);
    }
}
