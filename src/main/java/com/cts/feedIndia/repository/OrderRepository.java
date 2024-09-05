package com.cts.feedIndia.repository;


import java.sql.Timestamp;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.feedIndia.entity.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{
	Page<Order> findByConsumerId(int consumerId, Pageable pageable);
	Page<Order> findByCity(String city, Pageable pageable);
	Page<Order> findByPickedUpDateTimeBetween(Timestamp startDateTime, Timestamp endDateTime,Pageable pageable);
	Page<Order> findByCityAndPickedUpDateTimeBetween(String city,Timestamp startDateTime, Timestamp endDateTime,Pageable pageable);	
}
