package com.cts.feedIndia.services.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.Food;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;

public interface FoodService {
	public Food addFood(Food food) throws AlreadyExistException;
	
	public Food findFoodById(int id) throws NotFoundException;
	
	public List<Food> findFoodByCity(String city, Pageable pagable) throws NotFoundException;
	
	public List<Food> findFoodByCityAndDate(String city,Date date, Pageable pagable) throws NotFoundException;
	
	public List<Food> findFoodByCityAndFoodType(String city,FoodType type, Pageable pagable) throws NotFoundException;
	
	public List<Food> findFoodByCityAndDateAndFoodType(String city,Date date,FoodType type, Pageable pagable) throws NotFoundException;
	
	public Food updateFoodById(int id, Food food) throws NotFoundException;
	
	public List<Food> findFoodByUserId(int userId,Pageable pageable) throws NotFoundException;
	
	public List<Food> findAllFood(Pageable pageable) throws NotFoundException;
	
	public boolean deleteFoodById(int id) throws NotFoundException; 
}
