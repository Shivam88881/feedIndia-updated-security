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

import com.cts.feedIndia.entity.Transaction;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.TransactionServiceImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    TransactionController transactionController;

    @Mock
    TransactionServiceImpl transactionService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testAddTransaction() throws AlreadyExistException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User donor = new User();
        donor.setId(1);

        Transaction transaction = new Transaction();
        transaction.setDonor(donor);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(donor));
        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> responseEntity = transactionController.addTransaction(transaction);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testFindTransactionById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(transactionService.findTransactionById(anyInt())).thenReturn(new Transaction());

        ResponseEntity<Transaction> responseEntity = transactionController.findTransactionById(1);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindTransactionByDonorId() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(transactionService.findTransactionByDonorId(anyInt(), any(Pageable.class))).thenReturn(Collections.singletonList(new Transaction()));

        ResponseEntity<List<Transaction>> responseEntity = transactionController.findTransactionByDonorId(1, 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindTransactionByDate() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(transactionService.findTransactionByDate(any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(Collections.singletonList(new Transaction()));

        ResponseEntity<List<Transaction>> responseEntity = transactionController.findTransactionByDate(Date.valueOf(LocalDate.now()).toString(), Date.valueOf(LocalDate.now()).toString(), 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testFindTransactionByDonorIdAndDate() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(transactionService.findTransactionByDonorIdAndDate(anyInt(), any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(Collections.singletonList(new Transaction()));

        ResponseEntity<List<Transaction>> responseEntity = transactionController.findTransactionByDonorIdAndDate(1, Date.valueOf(LocalDate.now()).toString(), Date.valueOf(LocalDate.now()).toString(), 0);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpdateTransactionById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(transactionService.updateTransaction(anyInt(), any(Transaction.class))).thenReturn(new Transaction());

        ResponseEntity<Transaction> responseEntity = transactionController.updateTransactionById(1, new Transaction());

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteTransactionById() throws NotFoundException, BadRequestException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(transactionService.deleteTransactionById(anyInt())).thenReturn(true);

        ResponseEntity<String> responseEntity = transactionController.deleteTransactionById(1);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}

