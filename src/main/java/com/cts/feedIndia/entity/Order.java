package com.cts.feedIndia.entity;

import java.sql.Timestamp;


import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.constant.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@NonNull
	@Column(name="food_type")
	private FoodType foodType = FoodType.ANY;
	
	@Column(name="amount")
	private Integer amount;
	
	
	@NonNull
	@Column(name="pick_up_address")
	private String pickUpAddress;
	
	@Column(name="delivery_address")
	private String deliveryAddress;
	
	@NonNull
	@Column(name="order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus=OrderStatus.PROCESSING;;
	
	@NonNull
	@Column(name="donor_ids")
	private String donorIds;    // ids of all donor's seperated by comma
	
	@NonNull
	@Column(name="food_ids")
	private String foodIds;     // ids of all food's seperated by comma
	
	@NonNull
	@Column(name="pickup_date_time")
	private Timestamp pickedUpDateTime;
	

	@Column(name="city")
	private String city;
	
	
	@Column(name="delivered_date_time")
	private Timestamp deliveredDateTime;
	
	@ManyToOne
    @JoinColumn(name = "consumer_id")
    private User consumer;
}
