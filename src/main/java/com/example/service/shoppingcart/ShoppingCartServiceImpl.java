package com.example.service.shoppingcart;

import com.example.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.mapper.ShoppingCartMapper;
import com.example.model.ShoppingCart;
import com.example.model.User;
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
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCartResponseDto findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException(
                                "User not found by email: " + userEmail
                        )
                );

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException(
                                "ShoppingCart not found by user_id: " + user.getId()
                        )
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }
}
