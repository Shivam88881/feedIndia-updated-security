package com.cts.feedIndia.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.constant.TransactionStatus;
import com.cts.feedIndia.entity.Transaction;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.TransactionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void testAddTransaction() throws AlreadyExistException {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.addTransaction(transaction);

        assertNotNull(result);
    }

    @Test
    public void testFindTransactionById() throws NotFoundException {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.findTransactionById(1);

        assertNotNull(result);
    }

    @Test
    public void testFindTransactionByDonorId() throws NotFoundException {
        Transaction transaction = new Transaction();
        when(transactionRepository.findByDonorId(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(transaction)));

        List<Transaction> result = transactionService.findTransactionByDonorId(1, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindTransactionByDate() throws NotFoundException {
        Transaction transaction = new Transaction();
        Timestamp start = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        Timestamp end = Timestamp.valueOf(LocalDateTime.now());
        when(transactionRepository.findByDonationDateTimeBetween(any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(transaction)));

        List<Transaction> result = transactionService.findTransactionByDate(start, end, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindTransactionByDonorIdAndDate() throws NotFoundException {
        Transaction transaction = new Transaction();
        Timestamp start = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        Timestamp end = Timestamp.valueOf(LocalDateTime.now());
        when(transactionRepository.findByDonorIdAndDonationDateTimeBetween(anyInt(), any(Timestamp.class), any(Timestamp.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(transaction)));

        List<Transaction> result = transactionService.findTransactionByDonorIdAndDate(1, start, end, PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
    }

    @Test
    public void testUpdateTransaction() throws NotFoundException {
        Transaction oldTransaction = new Transaction();
        Transaction newTransaction = new Transaction();
        newTransaction.setStatus(TransactionStatus.SUCCESS);
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.of(oldTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(newTransaction);

        Transaction result = transactionService.updateTransaction(1, newTransaction);

        assertNotNull(result);
        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
    }

    @Test
    public void testDeleteTransactionById() throws NotFoundException {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.of(transaction));

        boolean result = transactionService.deleteTransactionById(1);

        assertTrue(result);
    }
}

