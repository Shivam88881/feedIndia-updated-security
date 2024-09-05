package com.cts.feedIndia.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cts.feedIndia.entity.Order;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.OrderServiceImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderServiceImpl orderService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testAddOrder() throws AlreadyExistException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User consumer = new User();
        consumer.setId(1);

        Order order = new Order();
        order.setConsumer(consumer);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(consumer));
        when(orderService.addOrder(any(Order.class))).thenReturn(order);

        ResponseEntity<Order> responseEntity = orderController.addOrder(order);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }


    @Test
    public void testFindOrderById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.findOrderById(anyInt())).thenReturn(new Order());

        ResponseEntity<Order> responseEntity = orderController.findOrderById(1, false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindOrderByConsumerId() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.findOrderByConsumerId(anyInt(), any(Pageable.class))).thenReturn(Collections.singletonList(new Order()));

        ResponseEntity<List<Order>> responseEntity = orderController.findOrderByConsumerId(1, 0, false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindOrderByCity() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.findOrderByCity(anyString(), any(Pageable.class))).thenReturn(Collections.singletonList(new Order()));

        ResponseEntity<List<Order>> responseEntity = orderController.findOrderByCity("New York", 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindOrderByDate() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.findOrderByDate(any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(Collections.singletonList(new Order()));

        ResponseEntity<List<Order>> responseEntity = orderController.findOrderByDate(Date.valueOf(LocalDate.now()).toString(), Date.valueOf(LocalDate.now()).toString(), 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindOrderByCityAndDate() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.findOrderByCityAndDate(anyString(), any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(Collections.singletonList(new Order()));

        ResponseEntity<List<Order>> responseEntity = orderController.findOrderByCityAndDate("New York", Date.valueOf(LocalDate.now()).toString(), Date.valueOf(LocalDate.now()).toString(), 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpdateOrderById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.updateOrderById(anyInt(), any(Order.class))).thenReturn(new Order());

        ResponseEntity<Order> responseEntity = orderController.updateOrderById(1, new Order());

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteOrderById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderService.deleteOrderById(anyInt())).thenReturn(true);

        ResponseEntity<String> responseEntity = orderController.deleteOrderById(1);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}

