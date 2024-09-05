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
@Table(name="food")
public class Food {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="native")
	@GenericGenerator(name="native")
	@Column(name="id")
	private Integer id;
	
	@NonNull
	@Column(name="food_name")
	private String foodName;
	
	@NonNull
	@Column(name="food_type")
	@Enumerated(EnumType.STRING)
	private FoodType foodType;
	
	@NonNull
	@Column(name="city")
	private String city;
	
	@NonNull
	@Column(name="pick_up_date")
	private Date pickupDate;
	
	@NonNull
	@Column(name="pick_up_time")
	private Time pickupTime;
	
	
	@Column(name="amount")
	private Integer amount;
	
	@Column(name="posted_amount")
	private Integer postedAmount;
	

	@Column(name="available")
	private Boolean available;
	
	@NonNull
	@Column(name="pick_up_address")
	private String pickupAddress;
	

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
