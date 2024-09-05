package com.example.controller;

import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.model.User;
import com.example.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for management shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Create cart items in shopping cart for logging user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ShoppingCartResponseDto addCartItem(Authentication authentication,
                                               @RequestBody @Valid CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.save(user, requestDto);
    }

    @GetMapping
    @Operation(summary = "Get shopping cart for logging user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user);
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update quantity for cart items by id in shopping cart")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ShoppingCartResponseDto updateCartItem(Authentication authentication,
                                      @PathVariable Long id,
                                      @RequestBody CartItemUpdateDto updateDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateById(user, id, updateDto);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete cart item by id")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void deleteCartItem(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
    }
}
