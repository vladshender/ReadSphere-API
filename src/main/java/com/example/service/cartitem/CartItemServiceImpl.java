package com.example.service.cartitem;

import com.example.dto.cartitem.CartItemDto;
import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.mapper.CartItemMapper;
import com.example.model.CartItem;
import com.example.model.ShoppingCart;
import com.example.model.User;
import com.example.repository.book.BookRepository;
import com.example.repository.cartitem.CartItemRepository;
import com.example.repository.shoppingcart.ShoppingCartRepository;
import com.example.repository.user.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDto save(CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        ShoppingCart shoppingCartByAuthUser = getShoppingCart();
        cartItem.setShoppingCart(shoppingCartByAuthUser);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemDto updateById(Long id, CartItemUpdateDto updateDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Not found CartItem by id: " + id
                        )
                );
        cartItemMapper.updateCartItemFromDto(updateDto, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    public ShoppingCart getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException(
                                "User not found by email: " + userEmail
                        )
                );

        return shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException(
                                "ShoppingCart not found by user_id: " + user.getId()
                        )
                );
    }
}
