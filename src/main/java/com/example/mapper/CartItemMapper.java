package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.cartitem.CartItemDto;
import com.example.dto.cartitem.CartItemRequestDto;
import com.example.dto.cartitem.CartItemUpdateDto;
import com.example.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Named("cartItemToDto")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(CartItemRequestDto requestDto);

    void updateCartItemFromDto(CartItemUpdateDto cartItemUpdateDto,
                               @MappingTarget CartItem cartItem);
}
