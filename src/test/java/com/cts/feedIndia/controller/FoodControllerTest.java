package com.cts.feedIndia.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.Food;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.FoodServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodControllerTest {

    @InjectMocks
    FoodController foodController;

    @Mock
    FoodServiceImpl foodService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testAddFood() throws AlreadyExistException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User user = new User();
        user.setId(1);

        Food food = new Food();
        food.setUser(user);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(foodService.addFood(any(Food.class))).thenReturn(food);

        ResponseEntity<Food> responseEntity = foodController.addFood(food);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }


    @Test
    public void testFindFoodById() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodService.findFoodById(anyInt())).thenReturn(new Food());

        ResponseEntity<Food> responseEntity = foodController.findFoodById(1, false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindFoodByCity() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodService.findFoodByCity(anyString(), any(Pageable.class))).thenReturn(Collections.singletonList(new Food()));

        ResponseEntity<List<Food>> responseEntity = foodController.findFoodByCity(0, "New York", false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindFoodByCityAndDate() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodService.findFoodByCityAndDate(anyString(), any(Date.class), any(Pageable.class))).thenReturn(Collections.singletonList(new Food()));

        ResponseEntity<List<Food>> responseEntity = foodController.findFoodByCityAndDate("New York", Date.valueOf(LocalDate.now()), 0, false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindFoodByCityAndDateAndFoodType() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodService.findFoodByCityAndDateAndFoodType(anyString(), any(Date.class), any(FoodType.class), any(Pageable.class))).thenReturn(Collections.singletonList(new Food()));

        ResponseEntity<List<Food>> responseEntity = foodController.findFoodByCityAndDateAndFoodType("New York", Date.valueOf(LocalDate.now()), FoodType.VEG, 0, false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpdateFoodById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodService.updateFoodById(anyInt(), any(Food.class))).thenReturn(new Food());

        ResponseEntity<Food> responseEntity = foodController.updateFoodById(1, new Food());

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteFoodById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodService.deleteFoodById(anyInt())).thenReturn(true);

        ResponseEntity<String> responseEntity = foodController.deleteFoodById(1);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}

