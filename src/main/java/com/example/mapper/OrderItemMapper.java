package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.orderitem.OrderItemResponseDto;
import com.example.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Named("itemToDto")
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDtoList(List<OrderItem> dtoList);

    OrderItem toModel(OrderItem orderItem);
}
