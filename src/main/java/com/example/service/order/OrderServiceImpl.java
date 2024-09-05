package com.example.service.order;

import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderUpdateStatusRequestDto;
import com.example.dto.orderitem.OrderItemResponseDto;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.model.CartItem;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.ShoppingCart;
import com.example.model.User;
import com.example.repository.book.BookRepository;
import com.example.repository.order.OrderRepository;
import com.example.repository.orderitem.OrderItemsRepository;
import com.example.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrderByUser(User user, OrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find shopping cart by user id: " + user.getId()));
        Order order = orderMapper.toModel(requestDto);
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        Set<OrderItem> orderItemSet = shoppingCart.getCartItems()
                .stream()
                .map(cart -> mapToOrderItem(cart, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItemSet);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemSet) {
            totalPrice = totalPrice.add(orderItem.getPrice());
        }
        order.setTotal(totalPrice);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> getOrderByUser(User user) {
        List<Order> orders = orderRepository.findAllByUser(user);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException(
                    "Can't find order list for user by id: " + user.getId());
        }
        return orderMapper.toListDto(orders);
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatusByOrder(Long id,
            OrderUpdateStatusRequestDto responseDto) {
        Order orderByUser = findById(id);
        orderMapper.updateOrderFromDto(responseDto, orderByUser);
        return orderMapper.toDto(orderRepository.save(orderByUser));
    }

    @Override
    @Transactional
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long id) {
        Order order = findById(id);
        return orderItemMapper.toDtoList(orderItemsRepository.findAllByOrder(order));
    }

    @Override
    public OrderItemResponseDto getOrderItemFromOrderById(Long orderId, Long orderItemsId) {
        Order order = findById(orderId);
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemsId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find item by id: " + orderItemsId));
        return orderItemMapper.toDto(orderItem);
    }

    private OrderItem mapToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(bookRepository.findById(cartItem.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find book by id: " + cartItem.getId())));
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(orderItem.getBook().getPrice()
                .multiply(new BigDecimal(orderItem.getQuantity())));
        return orderItem;
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find order by id: " + id));
    }
}
