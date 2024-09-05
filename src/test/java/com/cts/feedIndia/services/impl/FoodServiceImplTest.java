package com.cts.feedIndia.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.Food;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.FoodRepository;

import org.springframework.data.domain.Pageable;


import java.util.Collections;
import java.sql.Date;

import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FoodServiceImplTest {

    @InjectMocks
    private FoodServiceImpl foodService;

    @Mock
    private FoodRepository foodRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFood() throws AlreadyExistException {
        Food food = new Food();
        food.setFoodName("Pizza");

        when(foodRepository.save(any(Food.class))).thenReturn(food);

        Food result = foodService.addFood(food);

        assertNotNull(result);
        assertEquals("Pizza", result.getFoodName());
    }

    @Test
    public void testFindFoodById() throws NotFoundException {
        Food food = new Food();
        food.setId(1);

        when(foodRepository.findById(anyInt())).thenReturn(Optional.of(food));

        Food result = foodService.findFoodById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testFindFoodByCity() {
        Food food = new Food();
        food.setCity("New York");

        when(foodRepository.findByCity(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(food)));

        List<Food> result = foodService.findFoodByCity("New York", PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getCity());
    }

    @Test
    public void testFindFoodByCityAndDate() {
        Food food = new Food();
        food.setCity("New York");
        food.setPickupDate(new Date(0));

        when(foodRepository.findByCityDate(anyString(), any(Date.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(food)));

        List<Food> result = foodService.findFoodByCityAndDate("New York", new Date(0), PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getCity());
    }

    @Test
    public void testFindFoodByCityAndDateAndFoodType() {
        Food food = new Food();
        food.setCity("New York");
        food.setPickupDate(new Date(0));
        food.setFoodType(FoodType.VEG);

        when(foodRepository.findByCityDateType(anyString(), any(Date.class), any(FoodType.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(food)));

        List<Food> result = foodService.findFoodByCityAndDateAndFoodType("New York", new Date(0), FoodType.VEG, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getCity());
        assertEquals(FoodType.VEG, result.get(0).getFoodType());
    }

    @Test
    public void testUpdateFoodById() throws NotFoundException {
        Food oldFood = new Food();
        oldFood.setId(1);
        oldFood.setFoodName("Old Name");
        oldFood.setAmount(10);

        Food newFood = new Food();
        newFood.setFoodName("New Name");
        newFood.setAmount(20); // Set the amount on newFood, not oldFood

        when(foodRepository.findById(anyInt())).thenReturn(Optional.of(oldFood));
        when(foodRepository.save(any(Food.class))).thenReturn(newFood);

        Food result = foodService.updateFoodById(1, newFood);

        assertNotNull(result);
        assertEquals("New Name", result.getFoodName());
    }


    @Test
    public void testDeleteFoodById() throws NotFoundException {
        Food food = new Food();
        food.setId(1);

        when(foodRepository.findById(anyInt())).thenReturn(Optional.of(food));

        boolean result = foodService.deleteFoodById(1);

        assertTrue(result);
    }
}

