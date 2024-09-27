package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.orderitem.OrderItemResponseDto;
import com.example.model.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Named("itemToDto")
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @IterableMapping(qualifiedByName = "itemToDto")
    List<OrderItemResponseDto> toOrderItemDtoList(Set<OrderItem> dtoList);

    OrderItem toModel(OrderItem orderItem);
}
