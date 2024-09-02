package com.example.service.shoppingcart;

import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.mapper.CartItemMapper;
import com.example.mapper.ShoppingCartMapper;
import com.example.model.CartItem;
import com.example.model.ShoppingCart;
import com.example.model.User;
import com.example.repository.book.BookRepository;
import com.example.repository.cartitem.CartItemRepository;
import com.example.repository.shoppingcart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public ShoppingCartResponseDto save(User user, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find book by id: "
                                + requestDto.getBookId()
                )));
        ShoppingCart shoppingCartByUser = findShoppingCartByUser(user);
        cartItem.setShoppingCart(shoppingCartByUser);
        CartItem savedCart = cartItemRepository.saveAndFlush(cartItem);
        shoppingCartByUser.getCartItems().add(savedCart);
        return shoppingCartMapper.toDto(shoppingCartByUser);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateById(User user, Long id, CartItemUpdateDto updateDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                                "Not found CartItem by id: " + id
                        )
                );
        cartItemMapper.updateCartItemFromDto(updateDto, cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(findShoppingCartByUser(user));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(User user) {
        return shoppingCartMapper.toDto(findShoppingCartByUser(user));
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart findShoppingCartByUser(User user) {
        return shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(
                                "ShoppingCart not found by user_id: " + user.getId()
                        )
                );
    }
}
