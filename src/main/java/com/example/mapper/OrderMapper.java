package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderUpdateStatusRequestDto;
import com.example.model.Order;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderDate", source = "orderDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "orderItemsList", source = "orderItems", qualifiedByName = "itemToDto")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toListDto(List<Order> orderList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    Order toModel(OrderRequestDto requestDto);

    void updateOrderFromDto(OrderUpdateStatusRequestDto requestDto, @MappingTarget Order order);
}
