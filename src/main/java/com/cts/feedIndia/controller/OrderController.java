package com.cts.feedIndia.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.feedIndia.entity.Order;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.OrderServiceImpl;

@RestController
@RequestMapping("/v1/api/order")
public class OrderController {
	
	private int pageSize=15;

    @Autowired
    private OrderServiceImpl orderService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) throws AlreadyExistException, BadRequestException {
    	User consumer = userRepository.findById(order.getConsumer().getId()).get();
        order.setConsumer(consumer);
        Order newOrder = orderService.addOrder(order);
        if (newOrder == null) {
            throw new AlreadyExistException("Order creation failed");
        }
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable int id, @RequestParam(required = false) boolean includeConsumer) throws NotFoundException, BadRequestException {
        Order order = orderService.findOrderById(id);
        if (order == null) {
            throw new BadRequestException("No Order found with the provided ID");
        }
        if(!includeConsumer) {
            order.setConsumer(null);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/consumer/{consumerId}/page/{page}")
    public ResponseEntity<List<Order>> findOrderByConsumerId(@PathVariable int consumerId, @PathVariable int page, @PathVariable(required=false) boolean sortByDeliveryDate) throws NotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(page, this.pageSize, Sort.by("pickedUpDateTime"));
        List<Order> orders = orderService.findOrderByConsumerId(consumerId, pageable);
        if (orders.isEmpty()) {
            throw new BadRequestException("No Orders found for the provided consumer ID");
        }
        
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/city/{city}/page/{page}")
    public ResponseEntity<List<Order>> findOrderByCity(@PathVariable String city, @PathVariable int page) throws NotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Order> orders = orderService.findOrderByCity(city, pageable);
        
        if (orders.isEmpty()) {
            throw new BadRequestException("No Orders found for the provided city");
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/date/page/{page}")
    public ResponseEntity<List<Order>> findOrderByDate(@RequestParam("start") String startStr, @RequestParam("end") String endStr, @PathVariable int page) throws NotFoundException, BadRequestException {
        Timestamp start = Timestamp.valueOf(startStr + " 00:00:00");
        Timestamp end = Timestamp.valueOf(endStr + " 23:59:59");
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("pickedUpDateTime"));
        List<Order> orders = orderService.findOrderByDate(start, end, pageable);
        
        if (orders.isEmpty()) {
            throw new BadRequestException("No Orders found for the provided date range");
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @GetMapping("/city/{city}/date/page/{page}")
    public ResponseEntity<List<Order>> findOrderByCityAndDate(@PathVariable String city, @RequestParam("start") String startStr, @RequestParam("end") String endStr, @PathVariable int page) throws NotFoundException, BadRequestException {
    	Timestamp start = Timestamp.valueOf(startStr + " 00:00:00");
        Timestamp end = Timestamp.valueOf(endStr + " 23:59:59");
    	Pageable pageable = PageRequest.of(page, pageSize,Sort.by("pickedUpDateTime"));
        List<Order> orders = orderService.findOrderByCityAndDate(city, start, end, pageable);
        if (orders.isEmpty()) {
            throw new BadRequestException("No Orders found for the provided city and date range");
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    
    @GetMapping("/page/{page}")
    public ResponseEntity<List<Order>> findAllOrders(@PathVariable int page) throws NotFoundException, BadRequestException {
    	
    	Pageable pageable = PageRequest.of(page, pageSize,Sort.by("pickedUpDateTime"));
        List<Order> orders = orderService.findAllOrder(pageable);
        if (orders.isEmpty()) {
            throw new BadRequestException("No Orders found");
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrderById(@PathVariable int id, @RequestBody Order order) throws NotFoundException, BadRequestException {
    	Order updatedOrder = orderService.updateOrderById(id, order);
        if (updatedOrder == null) {
            throw new BadRequestException("Failed to update the Order");
        }
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable int id) throws NotFoundException, BadRequestException {
        boolean isRemoved = orderService.deleteOrderById(id);
        if (!isRemoved) {
            throw new BadRequestException("Failed to delete the Order");
        }
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }
}
