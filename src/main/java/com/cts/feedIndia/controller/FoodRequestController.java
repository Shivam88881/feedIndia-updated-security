package com.cts.feedIndia.controller;

import java.sql.Date;
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

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.FoodRequest;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.FoodRequestServiceImpl;

@RestController
@RequestMapping("/v1/api/foodRequest")
public class FoodRequestController {

    @Autowired
    private FoodRequestServiceImpl foodRequestService;
    
    @Autowired
    private UserRepository userRepository;
    
    private int pageSize=15;

    @PostMapping("/create")
    public ResponseEntity<FoodRequest> addFoodRequest(@RequestBody FoodRequest foodRequest) throws AlreadyExistException, BadRequestException {
        
    	User user = userRepository.findById(foodRequest.getUser().getId()).get();
    	foodRequest.setUser(user);
    	foodRequest.setFullfilled(false);
    	foodRequest.setRequestedAmount(foodRequest.getAmount());
    	FoodRequest newFoodRequest = foodRequestService.addFoodRequest(foodRequest);
        if (newFoodRequest == null) {
            throw new BadRequestException("FoodRequest creation failed");
        }
        return new ResponseEntity<>(newFoodRequest, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FoodRequest> findFoodRequestById(@PathVariable int id, @RequestParam(required = false) boolean includeUser) throws NotFoundException {
        FoodRequest foodRequest = foodRequestService.findFoodRequestById(id);
        if (foodRequest == null) {
            throw new NotFoundException("No FoodRequest found with the provided ID");
        }
        if (!includeUser) {
            foodRequest.setUser(null);
        }
        return new ResponseEntity<>(foodRequest, HttpStatus.OK);
    }

    @GetMapping("/city/{city}/page/{page}")
    public ResponseEntity<List<FoodRequest>> findFoodRequestByCity(@PathVariable int page,@PathVariable String city) throws NotFoundException {		
    	Pageable pageable = PageRequest.of(page, this.pageSize);
        List<FoodRequest> foodRequests = foodRequestService.findFoodRequestByCity(city,pageable);
        if (foodRequests.isEmpty()) {
            throw new NotFoundException("No FoodRequests found in the provided city");
        }
        foodRequests.forEach(request -> {
            request.setUser(null);
        });
        return new ResponseEntity<>(foodRequests, HttpStatus.OK);
    }
    
    @GetMapping("/city/{city}/date/{date}/page/{page}")
    public ResponseEntity<List<FoodRequest>> findFoodRequestByCityAndDate(@PathVariable String city, @PathVariable Date date, @PathVariable int page) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize,Sort.by("pickupDate"));
        List<FoodRequest> foodRequests = foodRequestService.findFoodRequestByCityAndDate(city, date, pageable);
        if (foodRequests.isEmpty()) {
            throw new NotFoundException("No FoodRequests found for the provided city and date");
        }
        foodRequests.forEach(request -> {
            request.setUser(null);
        });
        return new ResponseEntity<>(foodRequests, HttpStatus.OK);
    }
    
    @GetMapping("/page/{page}")
    public ResponseEntity<List<FoodRequest>> findAllFoodRequest(@PathVariable int page) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize,Sort.by("pickupDate"));
        List<FoodRequest> foodRequests = foodRequestService.findAllFoodRequest(pageable);
        if (foodRequests.isEmpty()) {
            throw new NotFoundException("No FoodRequests found");
        }
        foodRequests.forEach(request -> {
            request.setUser(null);
        });
        return new ResponseEntity<>(foodRequests, HttpStatus.OK);
    }
    
    
    @GetMapping("/city/{city}/type/{type}/page/{page}")
    public ResponseEntity<List<FoodRequest>> findFoodRequestByCityAndFoodType(@PathVariable String city, @PathVariable FoodType type, @PathVariable int page) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize,Sort.by("pickupDate"));
        List<FoodRequest> foodRequests = foodRequestService.findFoodRequestByCityAndFoodType(city, type, pageable);
        if (foodRequests.isEmpty()) {
            throw new NotFoundException("No FoodRequests found for the provided city, and food type");
        }
        foodRequests.forEach(request -> {
            request.setUser(null);
        });
        return new ResponseEntity<>(foodRequests, HttpStatus.OK);
    }

    @GetMapping("/city/{city}/date/{date}/type/{type}/page/{page}")
    public ResponseEntity<List<FoodRequest>> findFoodRequestByCityAndDateAndFoodType(@PathVariable String city, @PathVariable Date date, @PathVariable FoodType type, @PathVariable int page) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize,Sort.by("pickupDate"));
        List<FoodRequest> foodRequests = foodRequestService.findFoodRequestByCityAndDateAndFoodType(city, date, type, pageable);
        if (foodRequests.isEmpty()) {
            throw new NotFoundException("No FoodRequests found for the provided city, date, and food type");
        }
        foodRequests.forEach(request -> {
            request.setUser(null);
        });
        return new ResponseEntity<>(foodRequests, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<FoodRequest> updateFoodRequestById(@PathVariable int id, @RequestBody FoodRequest foodRequest) throws NotFoundException, BadRequestException {
        FoodRequest updatedFoodRequest = foodRequestService.updateFoodRequestById(id, foodRequest);
        if (updatedFoodRequest == null) {
            throw new BadRequestException("Failed to update the FoodRequest");
        }
        updatedFoodRequest.setUser(null);
        return new ResponseEntity<>(updatedFoodRequest, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFoodRequestById(@PathVariable int id) throws NotFoundException, BadRequestException {
        boolean isRemoved = foodRequestService.deleteFoodRequestById(id);
        if (!isRemoved) {
            throw new BadRequestException("Failed to delete the FoodRequest");
        }
        return new ResponseEntity<>("FoodRequest deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/find/user/{userId}/page/{page}")
    public ResponseEntity<List<FoodRequest>> findFoodRequestByUserId(@PathVariable int userId, @PathVariable int page) throws NotFoundException{
    	
    	Pageable pageable = PageRequest.of(page, this.pageSize, Sort.by("pickupDate"));
    	List<FoodRequest> foodRequests = foodRequestService.findFoodRequestByUserId(userId, pageable);
    	if (foodRequests.isEmpty()) {
            throw new NotFoundException("No FoodRequests found for the provided userId");
        }
        foodRequests.forEach(request -> {
            request.setUser(null);
        });
        return new ResponseEntity<>(foodRequests, HttpStatus.OK);
    }
}

