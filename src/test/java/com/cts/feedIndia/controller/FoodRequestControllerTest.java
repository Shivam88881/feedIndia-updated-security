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
import com.cts.feedIndia.entity.FoodRequest;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.FoodRequestServiceImpl;

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
public class FoodRequestControllerTest {

    @InjectMocks
    FoodRequestController foodRequestController;

    @Mock
    FoodRequestServiceImpl foodRequestService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testAddFoodRequest() throws AlreadyExistException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User user = new User();
        user.setId(1);

        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setUser(user);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(foodRequestService.addFoodRequest(any(FoodRequest.class))).thenReturn(foodRequest);

        ResponseEntity<FoodRequest> responseEntity = foodRequestController.addFoodRequest(foodRequest);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }


    @Test
    public void testFindFoodRequestById() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.findFoodRequestById(anyInt())).thenReturn(new FoodRequest());

        ResponseEntity<FoodRequest> responseEntity = foodRequestController.findFoodRequestById(1, false);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindFoodRequestByCity() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.findFoodRequestByCity(anyString(), any(Pageable.class))).thenReturn(Collections.singletonList(new FoodRequest()));

        ResponseEntity<List<FoodRequest>> responseEntity = foodRequestController.findFoodRequestByCity(0, "New York");

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindFoodRequestByUserId() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.findFoodRequestByUserId(anyInt(), any(Pageable.class))).thenReturn(Collections.singletonList(new FoodRequest()));

        ResponseEntity<List<FoodRequest>> responseEntity = foodRequestController.findFoodRequestByUserId(1, 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpdateFoodRequestById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.updateFoodRequestById(anyInt(), any(FoodRequest.class))).thenReturn(new FoodRequest());

        ResponseEntity<FoodRequest> responseEntity = foodRequestController.updateFoodRequestById(1, new FoodRequest());

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteFoodRequestById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.deleteFoodRequestById(anyInt())).thenReturn(true);

        ResponseEntity<String> responseEntity = foodRequestController.deleteFoodRequestById(1);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    public void testFindFoodRequestByCityAndDate() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.findFoodRequestByCityAndDate(anyString(), any(Date.class), any(Pageable.class))).thenReturn(Collections.singletonList(new FoodRequest()));

        ResponseEntity<List<FoodRequest>> responseEntity = foodRequestController.findFoodRequestByCityAndDate("New York", Date.valueOf(LocalDate.now()), 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindFoodRequestByCityAndDateAndFoodType() throws NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(foodRequestService.findFoodRequestByCityAndDateAndFoodType(anyString(), any(Date.class), any(FoodType.class), any(Pageable.class))).thenReturn(Collections.singletonList(new FoodRequest()));

        ResponseEntity<List<FoodRequest>> responseEntity = foodRequestController.findFoodRequestByCityAndDateAndFoodType("New York", Date.valueOf(LocalDate.now()), FoodType.VEG, 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}


