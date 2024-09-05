package com.cts.feedIndia.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cts.feedIndia.entity.Transaction;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.TransactionRepository;
import com.cts.feedIndia.services.service.TransactionService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository repository;
	
	@Override
	public Transaction addTransaction(Transaction transaction) throws AlreadyExistException {
		log.info("Adding transaction");
		Transaction newTransaction = repository.save(transaction);
		if(newTransaction != null) {
			log.info("Transaction added");
			return newTransaction;
		}else {
			log.info("Transaction creation failed");
			throw new AlreadyExistException("Transaction creation failed");
		}
	}
	
	@Override
	public Transaction findTransactionById(int id) throws NotFoundException {
		log.info("Finding transaction with id="+id);
		Optional<Transaction> transaction = repository.findById(id);
		if(transaction.isPresent()) {
			log.debug("Transaction found "+transaction.get());
			return transaction.get();
		}else {
			log.info("No transaction available with id="+id);
			throw new NotFoundException("No transaction available with id="+id);
		}
	}

	@Override
	public List<Transaction> findTransactionByDonorId(int id,Pageable pageable) throws NotFoundException {
		log.info("Finding all transaction of donor id= "+id);
		List<Transaction> transactions = repository.findByDonorId(id,pageable).getContent();
		if(transactions.isEmpty()){
			log.info("No transactions found of donor with id= "+id);
			throw new NotFoundException("No transactions found of donor with id= "+id);
		}
		log.info("All transactions found of donor with id= "+id);
		return transactions;
	}

	@Override
	public List<Transaction> findTransactionByDate(Timestamp start, Timestamp end,Pageable pageable) throws NotFoundException {
		log.info("Finding All Transactions from "+start+" to "+end);
		List<Transaction> transactions = repository.findByDonationDateTimeBetween(start, end, pageable).getContent();
		if(transactions.isEmpty()){
			log.info("No transactions found from "+start+" to "+end);
			throw new NotFoundException("No transactions found from "+start+" to "+end);
		}
		log.info("Found All Transactions from "+start+" to "+end);
		return transactions;
	}
	
	@Override
	public List<Transaction> findTransactionByDonorIdAndDate(int id, Timestamp start, Timestamp end,Pageable pageable) throws NotFoundException {
		log.info("Finding All Transactions of donor with id="+id+" from "+start+" to "+end);
		List<Transaction> transactions = repository.findByDonorIdAndDonationDateTimeBetween(id, start, end, pageable).getContent();
		if(transactions.isEmpty()){
			log.info("No transactions found of donor with id="+id+" from "+start+" to "+end);
			throw new NotFoundException("No transactions found of donor with id="+id+" from "+start+" to "+end);
		}
		log.info("Found All Transactions of donor with id="+id+" from "+start+" to "+end);
		return transactions;
	}

	@Override
	public Transaction updateTransaction(int id, Transaction newTransaction) throws NotFoundException {
		log.info("updating transaction with id="+id);
		Optional<Transaction> transaction = repository.findById(id);
		if(transaction.isEmpty()) {
			log.info("No transaction found with given id");
			throw new NotFoundException("No transaction found with given id");
		}else {
			Transaction oldTransaction = transaction.get();
			if(newTransaction.getStatus() != null) {
				oldTransaction.setStatus(newTransaction.getStatus());
			}
			repository.save(oldTransaction);
			log.info("Transaction updated with id="+id);
			return oldTransaction;
		}
	}

	@Override
	public boolean deleteTransactionById(int id) throws NotFoundException {
		log.info("deleting transaction with id="+id);
		Optional<Transaction> transaction = repository.findById(id);
		if(transaction.isPresent()) {
			repository.deleteById(id);
			log.info("Transaction deleted with id="+id);
			return true;
		}else {
			log.info("No transaction found with id="+id);
			throw new NotFoundException("No transaction found with id="+id);
		}
	}

	@Override
	public List<Transaction> findAllTransaction(Pageable pageable) throws NotFoundException {
		log.info("Finding All Transactions ");
		List<Transaction> transactions = repository.findAll(pageable).getContent();
		if(transactions.isEmpty()){
			log.info("No transactions found ");
			throw new NotFoundException("No transactions found ");
		}
		log.info("Found All Transactions ");
		return transactions;
	}
}
