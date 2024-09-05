package com.cts.feedIndia.entity;

import java.sql.Date;
import java.sql.Time;

import org.hibernate.annotations.GenericGenerator;

import com.cts.feedIndia.constant.FoodType;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name="food_request")
public class FoodRequest {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="native")
	@GenericGenerator(name="native")
	@Column(name="id")
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="food_type")
	@NonNull
	private FoodType foodType;
	
	@Column(name="city")
	@NonNull
	private String city;
	
	@Column(name="pick_up_date")
	@NonNull
	private Date pickupDate;	
	
	@Column(name="pick_up_time")
	@NonNull
	private Time pickupTime;
	
	@Column(name="amount")
	private Integer amount;
	
	@Column(name="posted_amount")
	private Integer requestedAmount;
	
	@Column(name="pick_up_address")
	@NonNull
	private String pickupAddress;
	
	
	@Column(name="message")
	@NonNull
	private String message;
	
	@Column(name="fullfilled")
	private Boolean fullfilled;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
