package com.cts.feedIndia.controller;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.Food;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.FoodServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@RestController
@RequestMapping("/v1/api/food")
public class FoodController {
	
	private int pageSize=15;

    @Autowired
    private FoodServiceImpl foodService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Food> addFood(@RequestBody Food food) throws AlreadyExistException, BadRequestException {

    	User user = userRepository.findById(food.getUser().getId()).get();
    	if(user==null) {
    		throw new BadRequestException("bas req");
    	}
    	food.setUser(user);
    	food.setPostedAmount(food.getAmount());
    	Food newFood = foodService.addFood(food);
        if (newFood == null) {
            throw new BadRequestException("Food creation failed");
        }
        return new ResponseEntity<>(newFood, HttpStatus.CREATED);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Food> findFoodById(@PathVariable int id, @RequestParam(required = false) boolean includeUser) throws NotFoundException {
        Food food = foodService.findFoodById(id);
        if (food == null) {
            throw new NotFoundException("No food found with the provided ID");
        }
        if (!includeUser) {
            food.setUser(null);  // Set the user to null if includeUser is false
        }
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping("/city/{city}/page/{page}")
    public ResponseEntity<List<Food>> findFoodByCity(@PathVariable int page,@PathVariable String city,@RequestParam(required=false) boolean includeUser) throws NotFoundException {
    	
    	Pageable pageable = PageRequest.of(page,this.pageSize,Sort.by("pickupDate"));
    	List<Food> foods = foodService.findFoodByCity(city, pageable);
    	if (foods.isEmpty()) {
            throw new NotFoundException("No food found for the provided city");
        }
    	if(!includeUser) {
        	foods.forEach(food -> {
                food.setUser(null);
            });
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
    
    @GetMapping("/city/{city}/date/{date}/page/{page}")
    public ResponseEntity<List<Food>> findFoodByCityAndDate(@PathVariable String city, @PathVariable Date date, @PathVariable int page,@RequestParam(required=false) boolean includeUser) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Food> foods = foodService.findFoodByCityAndDate(city, date, pageable);
        if (foods.isEmpty()) {
            throw new NotFoundException("No food found for the provided city and date");
        }     
        if(!includeUser) {
        	foods.forEach(food -> {
                food.setUser(null);
            });
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
    
    @GetMapping("/city/{city}/type/{type}/page/{page}")
    public ResponseEntity<List<Food>> findFoodByCityAndFoodType(@PathVariable String city,  @PathVariable FoodType type, @PathVariable int page, @RequestParam(required=false) boolean includeUser) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Food> foods = foodService.findFoodByCityAndFoodType(city, type, pageable);
        if (foods.isEmpty()) {
            throw new NotFoundException("No food found for the provided city, and food type");
        }
        if(!includeUser) {
        	foods.forEach(food -> {
                food.setUser(null);
            });
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/city/{city}/date/{date}/type/{type}/page/{page}")
    public ResponseEntity<List<Food>> findFoodByCityAndDateAndFoodType(@PathVariable String city, @PathVariable Date date, @PathVariable FoodType type, @PathVariable int page, @RequestParam(required=false) boolean includeUser) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Food> foods = foodService.findFoodByCityAndDateAndFoodType(city, date, type, pageable);
        if (foods.isEmpty()) {
            throw new NotFoundException("No food found for the provided city, date, and food type");
        }
        if(!includeUser) {
        	foods.forEach(food -> {
                food.setUser(null);
            });
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Food> updateFoodById(@PathVariable int id, @RequestBody Food food) throws NotFoundException, BadRequestException {
    	System.out.println(id+"......................................");
        Food updatedFood = foodService.updateFoodById(id, food);
        if (updatedFood == null) {
            throw new BadRequestException("Failed to update the food");
        }
        updatedFood.setUser(null);
        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFoodById(@PathVariable int id) throws NotFoundException, BadRequestException {
        boolean isRemoved = foodService.deleteFoodById(id);
        if (!isRemoved) {
            throw new BadRequestException("Failed to delete the food");
        }
        return new ResponseEntity<>("Food deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}/page/{page}")
    public ResponseEntity<List<Food>> getFoodByUserId(@PathVariable int userId,@PathVariable int page,@RequestParam(required=false) boolean includeUser) throws NotFoundException, BadRequestException{
    	Pageable pageable = PageRequest.of(page, this.pageSize);
    	List<Food> foods = foodService.findFoodByUserId(userId, pageable);
        if (foods.isEmpty()) {
            throw new NotFoundException("No food donation found by the provided user");
        }
        if(!includeUser) {
        	foods.forEach(food -> {
                food.setUser(null);
            });
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
    
    @GetMapping("/page/{page}")
    public ResponseEntity<List<Food>> getAllFood(@PathVariable int page,@RequestParam(required=false) boolean includeUser) throws NotFoundException, BadRequestException{
    	Pageable pageable = PageRequest.of(page, this.pageSize);
    	List<Food> foods = foodService.findAllFood(pageable);
        if (foods.isEmpty()) {
            throw new NotFoundException("No food donation found by the provided user");
        }
        if(!includeUser) {
        	foods.forEach(food -> {
                food.setUser(null);
            });
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
