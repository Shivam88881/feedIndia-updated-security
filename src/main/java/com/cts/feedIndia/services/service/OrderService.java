package com.cts.feedIndia.services.service;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.entity.Order;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;

public interface OrderService {
	public Order addOrder(Order order) throws AlreadyExistException;
	
	public Order findOrderById(int orderId) throws NotFoundException;
	
	public List<Order> findOrderByConsumerId(int consumerId,Pageable pageable) throws NotFoundException;
	
	public List<Order> findOrderByCity(String city,Pageable pageable) throws NotFoundException;
	
	public List<Order> findOrderByDate(Timestamp startDate, Timestamp endDate, Pageable pageable) throws NotFoundException;
	
	public List<Order> findAllOrder(Pageable pageable) throws NotFoundException;
	
	public List<Order> findOrderByCityAndDate(String city, Timestamp startDate, Timestamp endDate, Pageable pageable) throws NotFoundException;
	
	public Order updateOrderById(int orderId, Order order) throws NotFoundException;
	
	public boolean deleteOrderById(int OrderId) throws NotFoundException;
}
