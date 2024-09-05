package com.cts.feedIndia.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.constant.OrderStatus;
import com.cts.feedIndia.entity.Order;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.OrderRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void testAddOrder() throws AlreadyExistException {
        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.addOrder(order);

        assertNotNull(result);
    }

    @Test
    public void testFindOrderById() throws NotFoundException {
        Order order = new Order();
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        Order result = orderService.findOrderById(1);

        assertNotNull(result);
    }

    @Test
    public void testFindOrderByConsumerId() throws NotFoundException {
        Order order = new Order();
        when(orderRepository.findByConsumerId(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(order)));

        List<Order> result = orderService.findOrderByConsumerId(1, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindOrderByCity() throws NotFoundException {
        Order order = new Order();
        when(orderRepository.findByCity(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(order)));

        List<Order> result = orderService.findOrderByCity("New York", PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindOrderByDate() throws NotFoundException {
        Order order = new Order();
        Timestamp start = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        Timestamp end = Timestamp.valueOf(LocalDateTime.now());
        when(orderRepository.findByPickedUpDateTimeBetween(any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(order)));

        List<Order> result = orderService.findOrderByDate(start, end, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindOrderByCityAndDate() throws NotFoundException {
        Order order = new Order();
        Timestamp start = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        Timestamp end = Timestamp.valueOf(LocalDateTime.now());
        when(orderRepository.findByCityAndPickedUpDateTimeBetween(anyString(), any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(order)));

        List<Order> result = orderService.findOrderByCityAndDate("New York", start, end, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testUpdateOrderById() throws NotFoundException {
        Order oldOrder = new Order();
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.PROCESSING);;
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(oldOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(newOrder);

        Order result = orderService.updateOrderById(1, newOrder);

        assertNotNull(result);
        assertEquals(OrderStatus.PROCESSING, result.getOrderStatus());
    }

    @Test
    public void testDeleteOrderById() throws NotFoundException {
        Order order = new Order();
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        boolean result = orderService.deleteOrderById(1);

        assertTrue(result);
    }
}

