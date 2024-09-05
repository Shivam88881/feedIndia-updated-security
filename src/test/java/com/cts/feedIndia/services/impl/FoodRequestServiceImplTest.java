package com.cts.feedIndia.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.FoodRequest;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.FoodRequestRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodRequestServiceImplTest {

    @InjectMocks
    private FoodRequestServiceImpl foodRequestService;

    @Mock
    private FoodRequestRepository foodRequestRepository;

    @Test
    public void testAddFoodRequest() throws AlreadyExistException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setFoodType(FoodType.VEG);

        when(foodRequestRepository.save(any(FoodRequest.class))).thenReturn(foodRequest);

        FoodRequest result = foodRequestService.addFoodRequest(foodRequest);

        assertNotNull(result);
        assertEquals(FoodType.VEG, result.getFoodType());
    }

    @Test
    public void testFindFoodRequestById() throws NotFoundException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setId(1);

        when(foodRequestRepository.findById(anyInt())).thenReturn(Optional.of(foodRequest));

        FoodRequest result = foodRequestService.findFoodRequestById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testFindFoodRequestByCity() throws NotFoundException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setCity("New York");

        when(foodRequestRepository.findByCity(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(foodRequest)));

        List<FoodRequest> result = foodRequestService.findFoodRequestByCity("New York", PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getCity());
    }

    @Test
    public void testFindFoodRequestByCityAndDate() throws NotFoundException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setCity("New York");
        foodRequest.setPickupDate(Date.valueOf(LocalDate.now()));

        when(foodRequestRepository.findByCityDate(anyString(), any(Date.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(foodRequest)));

        List<FoodRequest> result = foodRequestService.findFoodRequestByCityAndDate("New York", Date.valueOf(LocalDate.now()), PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getCity());
    }

    @Test
    public void testFindFoodRequestByCityAndDateAndFoodType() throws NotFoundException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setCity("New York");
        foodRequest.setPickupDate(Date.valueOf(LocalDate.now()));
        foodRequest.setFoodType(FoodType.VEG);

        when(foodRequestRepository.findByCityDateType(anyString(), any(Date.class), any(FoodType.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(foodRequest)));

        List<FoodRequest> result = foodRequestService.findFoodRequestByCityAndDateAndFoodType("New York", Date.valueOf(LocalDate.now()), FoodType.VEG, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getCity());
        assertEquals(FoodType.VEG, result.get(0).getFoodType());
    }

    @Test
    public void testUpdateFoodRequestById() throws NotFoundException {
        FoodRequest oldFoodRequest = new FoodRequest();
        oldFoodRequest.setId(1);
        oldFoodRequest.setFoodType(FoodType.ANY);

        FoodRequest newFoodRequest = new FoodRequest();
        newFoodRequest.setFoodType(FoodType.VEG);
        newFoodRequest.setAmount(10); // Set an amount for newFoodRequest

        when(foodRequestRepository.findById(anyInt())).thenReturn(Optional.of(oldFoodRequest));
        when(foodRequestRepository.save(any(FoodRequest.class))).thenReturn(newFoodRequest);

        FoodRequest result = foodRequestService.updateFoodRequestById(1, newFoodRequest);

        assertNotNull(result);
        assertEquals(FoodType.VEG, result.getFoodType());
    }

    @Test
    public void testDeleteFoodRequestById() throws NotFoundException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setId(1);

        when(foodRequestRepository.findById(anyInt())).thenReturn(Optional.of(foodRequest));

        boolean result = foodRequestService.deleteFoodRequestById(1);

        assertTrue(result);
    }

    @Test
    public void testFindFoodRequestByUserId() throws NotFoundException {
        FoodRequest foodRequest = new FoodRequest();
        User user =new User();
        user.setId(1);
        foodRequest.setUser(user);

        when(foodRequestRepository.findByUserId(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(foodRequest)));

        List<FoodRequest> result = foodRequestService.findFoodRequestByUserId(1, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        assertEquals(1, result.get(0).getUser().getId());
    }
}

