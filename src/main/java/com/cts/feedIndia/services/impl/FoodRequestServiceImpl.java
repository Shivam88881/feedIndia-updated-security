package com.cts.feedIndia.services.impl;

import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.FoodRequest;
import com.cts.feedIndia.repository.FoodRequestRepository;
import com.cts.feedIndia.services.service.FoodRequestService;
import lombok.extern.slf4j.Slf4j;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FoodRequestServiceImpl implements FoodRequestService {

	@Autowired
	private FoodRequestRepository repository;
	
	@Override
	public FoodRequest addFoodRequest(FoodRequest foodRequest) throws AlreadyExistException {
		log.info("adding Food request");
		FoodRequest newFoodRequest = repository.save(foodRequest);
		if(newFoodRequest != null) {
			log.info("Food request added");
			return newFoodRequest;
		}else {
			log.info("Food request creation failed");
			throw new AlreadyExistException("Food request creation failed");
		}
	}

	@Override
	public FoodRequest findFoodRequestById(int id) throws NotFoundException {
		log.info("Finding food request with id="+id);
		Optional<FoodRequest> foodRequest = repository.findById(id);
		if(foodRequest.isPresent()) {
			log.info("Founded food request with id="+id);
			return foodRequest.get();
		}else {
			log.info("No food request found with id="+id);
			throw new NotFoundException("No food request found with id="+id);
		}
	}

	@Override
	public List<FoodRequest> findFoodRequestByCity(String city,Pageable pageable) throws NotFoundException {
		log.info("Finding food requests in "+city);
		List<FoodRequest> foodRequests = repository.findByCity(city,pageable).getContent();
		if(foodRequests.isEmpty()){
			log.info("No food requests found in "+city);
			throw new NotFoundException("No food requests found in "+city);
		}
		log.info("Founded food requests in "+city);
		return foodRequests;
	}

	@Override
	public List<FoodRequest> findFoodRequestByCityAndDate(String city, Date date,Pageable pageable) throws NotFoundException {
		log.info("Finding food requests in "+city +" on date "+date);
		List<FoodRequest> foodRequests = repository.findByCityDate(city, date, pageable).getContent();
		if(foodRequests.isEmpty()){
			log.info("No food requests found in "+city+" on date "+date);
			throw new NotFoundException("No food requests found in "+city+" on date "+date);
		}
		log.info("Founded food requests in "+city+" on date "+date);
		return foodRequests;
	}
	
	@Override
	public List<FoodRequest> findFoodRequestByCityAndFoodType(String city, FoodType type, Pageable pageable)
			throws NotFoundException {
		log.info("Finding food requests in "+city +" of type "+type.toString());
		List<FoodRequest> foodRequests = repository.findByCityAndType(city, type,pageable).getContent();
		if(foodRequests.isEmpty()){
			log.info("No food requests found in "+city +" of type "+type.toString());
			throw new NotFoundException("No food requests found in "+city +" of type "+type.toString());
		}
		log.info("Founded food requests in "+city +" of type "+type.toString());
		return foodRequests;
	}

	@Override
	public List<FoodRequest> findFoodRequestByCityAndDateAndFoodType(String city, Date date, FoodType type,Pageable pageable) throws NotFoundException {
		log.info("Finding food requests in "+city +" on date "+date+" of type "+type.toString());
		List<FoodRequest> foodRequests = repository.findByCityDateType(city, date, type,pageable).getContent();
		if(foodRequests.isEmpty()){
			log.info("No food requests found in "+city +" on date "+date+" of type "+type.toString());
			throw new NotFoundException("No food requests found in "+city +" on date "+date+" of type "+type.toString());
		}
		log.info("Founded food requests in "+city +" on date "+date+" of type "+type.toString());
		return foodRequests;
	}

	@Override
	public FoodRequest updateFoodRequestById(int id, FoodRequest newFoodRequest) throws NotFoundException {
		log.info("updating food request with id="+id);
		Optional<FoodRequest> foodRequest = repository.findById(id);
		if(foodRequest.isPresent()) {
			FoodRequest oldFoodRequest = foodRequest.get();
			if(newFoodRequest.getFoodType()!=null) {
				oldFoodRequest.setFoodType(newFoodRequest.getFoodType());
			}
			if(newFoodRequest.getCity()!=null) {
				oldFoodRequest.setCity(newFoodRequest.getCity());
			}
			if(newFoodRequest.getPickupDate()!=null) {
				oldFoodRequest.setPickupDate(newFoodRequest.getPickupDate());
			}
			if(newFoodRequest.getPickupTime()!=null) {
				oldFoodRequest.setPickupTime(newFoodRequest.getPickupTime());
			}
			if(newFoodRequest.getAmount() >=0) {
				oldFoodRequest.setAmount(newFoodRequest.getAmount());
			}
			if(newFoodRequest.getPickupAddress() !=null) {
				oldFoodRequest.setPickupAddress(newFoodRequest.getPickupAddress());
			}
			if(newFoodRequest.getFullfilled() != null) {
				oldFoodRequest.setFullfilled(newFoodRequest.getFullfilled());
			}
			if(newFoodRequest.getMessage() != null) {
				oldFoodRequest.setMessage(newFoodRequest.getMessage());
			}
			repository.save(oldFoodRequest);
			log.info("food request updated with id="+id);
			return oldFoodRequest;
		}else {
			log.info("No food request found with id="+id);
			throw new NotFoundException("No food request found with id="+id);
		}
	}

	@Override
	public boolean deleteFoodRequestById(int id) throws NotFoundException {
		log.info("deleting food request with id="+id);
		Optional<FoodRequest> foodRequest = repository.findById(id);
		if(foodRequest.isPresent()) {
			repository.deleteById(id);
			log.info("food request deleted with id="+id);
			return true;
		}else {
			log.info("No food request found with id="+id);
			throw new NotFoundException("No food request found with id="+id);
		}
	}

	@Override
	public List<FoodRequest> findFoodRequestByUserId(int userId,Pageable pageable) throws NotFoundException {
		log.info("finding food request posted by user id "+userId);
		List<FoodRequest> foodRequests = repository.findByUserId(userId,pageable).getContent();
		if(foodRequests.isEmpty()){
			log.info("No food requests found by user id "+userId);
			throw new NotFoundException("No food requests found by user id "+userId);
		}
		log.info("No food requests found by user id "+userId);
		return foodRequests;
		
	}

	@Override
	public List<FoodRequest> findAllFoodRequest(Pageable pageable) throws NotFoundException {
		log.info("finding food request posted");
		List<FoodRequest> foodRequests = repository.findAll(pageable).getContent();
		if(foodRequests.isEmpty()){
			log.info("No food requests found ");
			throw new NotFoundException("No food requests found by ");
		}
		log.info("No food requests found ");
		return foodRequests;
	}

	
}
