package com.cts.feedIndia.services.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.Food;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.FoodRepository;
import com.cts.feedIndia.services.service.FoodService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FoodServiceImpl implements FoodService{

	@Autowired
	private FoodRepository repository;
	
	@Override
	public Food addFood(Food food) throws AlreadyExistException {
		log.info("Creating Food");
		Food newFood = repository.save(food);
		if(newFood != null) {
			log.info("Food created");
			return newFood;
		}else {
			log.info("Food creation failed");
			throw new AlreadyExistException("Food creation failed");
		}
	}

	@Override
	public Food findFoodById(int id) throws NotFoundException {
		log.info("Finding food with id="+id);
		Optional<Food> food = repository.findById(id);
		if(food.isPresent()) {
			log.info("Founded food with id="+id);
			return food.get();
		}else {
			log.info("No food found with id="+id);
			throw new NotFoundException("No food found with id="+id);
		}
	}

	@Override
	public List<Food> findFoodByCity(String city, Pageable pagable) {
		log.info("Finding foods in "+city);
		List<Food> foods = repository.findByCity(city, pagable).getContent();
		log.info("Founded foods in "+city);
		return foods;
	}

	@Override
	public List<Food> findFoodByCityAndDate(String city, Date date, Pageable pagable) {
		log.info("Finding foods in "+city +" on date "+date);
		List<Food> foods = repository.findByCityDate(city, date, pagable).getContent();
		log.info("Founded foods in "+city+" on date "+date);
		return foods;
	}

	@Override
	public List<Food> findFoodByCityAndDateAndFoodType(String city, Date date, FoodType type, Pageable pagable) {
		log.info("Finding foods in "+city +" on date "+date+" of type "+type.toString());
		List<Food> foods = repository.findByCityDateType(city, date, type, pagable).getContent();
		log.info("Founded foods in "+city +" on date "+date+" of type "+type.toString());
		return foods;
	}

	@Override
	public Food updateFoodById(int id, Food newFood) throws NotFoundException {
		log.info("updating food with id="+id);
		Optional<Food> food = repository.findById(id);
		if(food.isPresent()) {
			Food oldFood = food.get();
			if(newFood.getFoodName()!=null) {
				oldFood.setFoodName(newFood.getFoodName());
			}
			if(newFood.getFoodType()!=null) {
				oldFood.setFoodType(newFood.getFoodType());
			}
			if(newFood.getCity()!=null) {
				oldFood.setCity(newFood.getCity());
			}
			if(newFood.getPickupDate()!=null) {
				oldFood.setPickupDate(newFood.getPickupDate());
			}
			if(newFood.getPickupTime()!=null) {
				oldFood.setPickupTime(newFood.getPickupTime());
			}
			if(newFood.getPickupAddress()!=null) {
				oldFood.setPickupAddress(newFood.getPickupAddress());
			}
			if(newFood.getAvailable()!= null) {
				oldFood.setAvailable(newFood.getAvailable());
			}
			if(newFood.getAmount()>=0) {
				oldFood.setAmount(newFood.getAmount());
			}
			repository.save(oldFood);
			log.info("food updated with id="+id);
			return oldFood;
		}else {
			log.info("No food found with id="+id);
			throw new NotFoundException("No food found with id="+id);
		}
	}

	@Override
	public boolean deleteFoodById(int id) throws NotFoundException {
		log.info("deleting food with id="+id);
		Optional<Food> food = repository.findById(id);
		if(food.isPresent()) {
			repository.deleteById(id);
			log.info("food deleted with id="+id);
			return true;
		}else {
			log.info("No food found with id="+id);
			throw new NotFoundException("No food found with id="+id);
		}
	}

	@Override
	public List<Food> findFoodByCityAndFoodType(String city, FoodType type, Pageable pagable) throws NotFoundException {
		log.info("Finding foods in "+city +" of type "+type.toString());
		List<Food> foods = repository.findByCityAndType(city,  type, pagable).getContent();
		log.info("Founded foods in "+city +" of type "+type.toString());
		return foods;
	}

	@Override
	public List<Food> findFoodByUserId(int userId, Pageable pageable) throws NotFoundException {
		log.info("Finding foods posted by user "+userId);
		List<Food> foods = repository.findByUserId(userId,pageable).getContent();
		log.info("Founded foods posted by user "+userId);
		return foods;
	}

	@Override
	public List<Food> findAllFood(Pageable pageable) throws NotFoundException {
		log.info("Finding foods posted by user ");
		List<Food> foods = repository.findAll(pageable).getContent();
		log.info("Founded foods posted by user ");
		return foods;
	}
}
