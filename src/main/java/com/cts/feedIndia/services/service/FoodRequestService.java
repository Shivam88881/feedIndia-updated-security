package com.cts.feedIndia.services.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.FoodRequest;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;

public interface FoodRequestService {
	public FoodRequest addFoodRequest(FoodRequest foodRequest) throws AlreadyExistException;
	
	public FoodRequest findFoodRequestById(int id) throws NotFoundException;
	
	public List<FoodRequest> findFoodRequestByUserId(int userId, Pageable pageable) throws NotFoundException;
	
	public List<FoodRequest> findAllFoodRequest(Pageable pageable) throws NotFoundException;
	
	public List<FoodRequest> findFoodRequestByCity(String city,Pageable pageable) throws NotFoundException;
	
	public List<FoodRequest> findFoodRequestByCityAndDate(String city,Date date,Pageable pageable) throws NotFoundException;
	
	public List<FoodRequest> findFoodRequestByCityAndFoodType(String city,FoodType type,Pageable pageable) throws NotFoundException;
	
	public List<FoodRequest> findFoodRequestByCityAndDateAndFoodType(String city,Date date,FoodType type,Pageable pageable) throws NotFoundException;
	
	public FoodRequest updateFoodRequestById(int id, FoodRequest foodRequest) throws NotFoundException;
	
	public boolean deleteFoodRequestById(int id) throws NotFoundException;
}
