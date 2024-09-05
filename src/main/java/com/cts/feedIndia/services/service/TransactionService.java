package com.cts.feedIndia.services.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cts.feedIndia.entity.Transaction;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;

public interface TransactionService {
	public Transaction addTransaction(Transaction transaction) throws AlreadyExistException;
	
	public Transaction findTransactionById(int id) throws NotFoundException;
	
	public List<Transaction> findTransactionByDonorId(int id,Pageable pageable) throws NotFoundException;
	
	public List<Transaction> findAllTransaction(Pageable pageable) throws NotFoundException;
	
	public List<Transaction> findTransactionByDate(Timestamp start, Timestamp end, Pageable pageable) throws NotFoundException;
	
	public List<Transaction> findTransactionByDonorIdAndDate(int id,Timestamp start, Timestamp end, Pageable pageable) throws NotFoundException;
	
	public Transaction updateTransaction(int id, Transaction transaction) throws NotFoundException;
	
	public boolean deleteTransactionById(int id) throws NotFoundException;
}
