package com.cts.feedIndia.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cts.feedIndia.entity.Order;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.OrderRepository;
import com.cts.feedIndia.services.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository repository;
	
	@Override
	public Order addOrder(Order order) throws AlreadyExistException {
		log.info("Creating order");
		Order newOrder = repository.save(order);
		if(newOrder != null) {
			log.info("Creating new order");
			return newOrder;
		}else {
			log.info("Order creation failed");
			throw new AlreadyExistException("Order creation failed");
		}
	}

	@Override
	public Order findOrderById(int orderId) throws NotFoundException {
		log.info("Finding order with id = "+orderId);
		Optional<Order> order = repository.findById(orderId);
		if(order.isPresent()) {
			log.debug("order found "+order.get());
			return order.get();
		}else {
			log.info("No order found with id ="+orderId);
			throw new NotFoundException("No order found with id ="+orderId);
		}
	}

	@Override
	public List<Order> findOrderByConsumerId(int consumerId, Pageable pageable) throws NotFoundException {
		log.info("Finding All order with consumer id = "+ consumerId);
		List<Order> orders = repository.findByConsumerId(consumerId, pageable).getContent();
		if(orders.isEmpty()){
			log.info("No orders found with consumer id = "+ consumerId);
			throw new NotFoundException("No orders found with consumer id = "+ consumerId);
		}
		log.info("All orders found with consumer id = "+ consumerId);
		return orders;
	}

	@Override
	public Order updateOrderById(int orderId, Order newOrder) throws NotFoundException {
	    log.info("Updating order with id="+orderId);
	    Optional<Order> order = repository.findById(orderId);
	    if(order.isEmpty()) {
	        log.info("No order found with given id");
	        throw new NotFoundException("No order found with given id");
	    } else {
	        Order oldOrder = order.get();
	        if(newOrder.getFoodType() != null) {
	            oldOrder.setFoodType(newOrder.getFoodType());
	        }
	        if(newOrder.getAmount() != null) {
	            oldOrder.setAmount(newOrder.getAmount());
	        }
	        if(newOrder.getPickUpAddress() != null) {
	            oldOrder.setPickUpAddress(newOrder.getPickUpAddress());
	        }
	        if(newOrder.getDeliveryAddress() != null) {
	            oldOrder.setDeliveryAddress(newOrder.getDeliveryAddress());
	        }
	        if(newOrder.getOrderStatus() != null) {
	            oldOrder.setOrderStatus(newOrder.getOrderStatus());
	        }
	        if(newOrder.getDonorIds() != null) {
	            oldOrder.setDonorIds(newOrder.getDonorIds());
	        }
	        if(newOrder.getFoodIds() != null) {
	            oldOrder.setFoodIds(newOrder.getFoodIds());
	        }
	        if(newOrder.getPickedUpDateTime() != null) {
	            oldOrder.setPickedUpDateTime(newOrder.getPickedUpDateTime());
	        }
	        if(newOrder.getCity() != null) {
	            oldOrder.setCity(newOrder.getCity());
	        }
	        if(newOrder.getDeliveredDateTime() != null) {
	            oldOrder.setDeliveredDateTime(newOrder.getDeliveredDateTime());
	        }
	        if(newOrder.getConsumer() != null) {
	            oldOrder.setConsumer(newOrder.getConsumer());
	        }
	        repository.save(oldOrder);
	        log.info("Order updated with id="+orderId);
	        return oldOrder;
	    }
	}


	@Override
	public boolean deleteOrderById(int orderId) throws NotFoundException {
		log.info("deleting order with id ="+orderId);
		Optional<Order> order = repository.findById(orderId);
		if(order.isPresent()) {
			repository.deleteById(orderId);
			log.info("Order deleted");
			return true;
		}else {
			log.info("No order found with id ="+orderId);
			throw new NotFoundException("No order found with id ="+orderId);
		}
	}

	@Override
	public List<Order> findOrderByCity(String city, Pageable pageable) throws NotFoundException {
		log.info("finding orders in city "+city);
		List<Order> orders = repository.findByCity(city, pageable).getContent();
		if(orders.isEmpty()){
			log.info("No orders found in city "+city);
			throw new NotFoundException("No orders found in city "+city);
		}
		log.info("Orders found in city "+city);
		return orders;
	}

	@Override
	public List<Order> findOrderByDate(Timestamp startDate, Timestamp endDate, Pageable pageable) throws NotFoundException {
		log.info("finding orders from "+startDate+" to "+endDate);
		List<Order> orders = repository.findByPickedUpDateTimeBetween(startDate, endDate, pageable).getContent();
		if(orders.isEmpty()){
			log.info("No orders found from "+startDate+" to "+endDate);
			throw new NotFoundException("No orders found from "+startDate+" to "+endDate);
		}
		log.info("Orders found from "+startDate+" to "+endDate);
		return orders;
	}

	@Override
	public List<Order> findOrderByCityAndDate(String city, Timestamp startDate, Timestamp endDate, Pageable pageable) throws NotFoundException {
		log.info("finding orders in "+city+" from "+startDate+" to "+endDate);
		List<Order> orders = repository.findByCityAndPickedUpDateTimeBetween(city, startDate, endDate, pageable).getContent();
		if(orders.isEmpty()){
			log.info("No orders found in "+city+" from "+startDate+" to "+endDate);
			throw new NotFoundException("No orders found in "+city+" from "+startDate+" to "+endDate);
		}
		log.info("Orders found in "+city+" from "+startDate+" to "+endDate);
		return orders;
	}

	@Override
	public List<Order> findAllOrder(Pageable pageable) throws NotFoundException {
		log.info("finding orders ");
		List<Order> orders = repository.findAll(pageable).getContent();
		if(orders.isEmpty()){
			log.info("No orders found");
			throw new NotFoundException("No orders found");
		}
		log.info("Orders found");
		return orders;
	}
	
}