package com.cts.feedIndia.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.cts.feedIndia.entity.Transaction;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.TransactionServiceImpl;

@RestController
@RequestMapping("/v1/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;
    
    @Autowired
    private UserRepository userRepository;
    
    private int pageSize=15;

    @PostMapping("/create")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) throws AlreadyExistException, BadRequestException {
        User donor = userRepository.findById(transaction.getDonor().getId()).get();
        transaction.setDonor(donor);
        Transaction newTransaction = transactionService.addTransaction(transaction);
        if (newTransaction == null) {
            throw new BadRequestException("Transaction creation failed");
        }
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Transaction> findTransactionById(@PathVariable int id) throws NotFoundException, BadRequestException {
        Transaction transaction = transactionService.findTransactionById(id);
        if (transaction == null) {
            throw new BadRequestException("No Transaction found with the provided ID");
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/donor/{donorId}/page/{page}")
    public ResponseEntity<List<Transaction>> findTransactionByDonorId(@PathVariable int donorId, @PathVariable int page) throws NotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Transaction> transactions = transactionService.findTransactionByDonorId(donorId, pageable);
        if (transactions.isEmpty()) {
            throw new BadRequestException("No Transactions found for the provided donor ID");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/date/page/{page}")
    public ResponseEntity<List<Transaction>> findTransactionByDate(@RequestParam("start") String startStr, @RequestParam("end") String endStr, @PathVariable int page) throws NotFoundException, BadRequestException {
        Timestamp start = Timestamp.valueOf(startStr + " 00:00:00");
        Timestamp end = Timestamp.valueOf(endStr + " 23:59:59");
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Transaction> transactions = transactionService.findTransactionByDate(start, end, pageable);
        if (transactions.isEmpty()) {
            throw new BadRequestException("No Transactions found for the provided date range");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/donor/{donorId}/date/page/{page}")
    public ResponseEntity<List<Transaction>> findTransactionByDonorIdAndDate(@PathVariable int donorId, @RequestParam("start") String startStr, @RequestParam("end") String endStr, @PathVariable int page) throws NotFoundException, BadRequestException {
        Timestamp start = Timestamp.valueOf(startStr + " 00:00:00");
        Timestamp end = Timestamp.valueOf(endStr + " 23:59:59");
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Transaction> transactions = transactionService.findTransactionByDonorIdAndDate(donorId, start, end, pageable);
        if (transactions.isEmpty()) {
            throw new BadRequestException("No Transactions found for the provided donor ID and date range");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    
    @GetMapping("/page/{page}")
    public ResponseEntity<List<Transaction>> findAllTransaction( @PathVariable int page) throws NotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(page, this.pageSize);
        List<Transaction> transactions = transactionService.findAllTransaction(pageable);
        if (transactions.isEmpty()) {
            throw new BadRequestException("No Transactions found ");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransactionById(@PathVariable int id, @RequestBody Transaction transaction) throws NotFoundException, BadRequestException {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        if (updatedTransaction == null) {
            throw new BadRequestException("Failed to update the Transaction");
        }
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable int id) throws NotFoundException, BadRequestException {
        boolean isRemoved = transactionService.deleteTransactionById(id);
        if (!isRemoved) {
            throw new BadRequestException("Failed to delete the Transaction");
        }
        return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.OK);
    }
}

