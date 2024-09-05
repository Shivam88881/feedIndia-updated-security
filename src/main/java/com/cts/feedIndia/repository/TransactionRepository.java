package com.cts.feedIndia.repository;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.feedIndia.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
	
	public Page<Transaction> findByDonorId(int id,Pageable pageable);
	
	public Page<Transaction> findByDonationDateTimeBetween(Timestamp start, Timestamp end,Pageable pageable);
	
	public Page<Transaction> findByDonorIdAndDonationDateTimeBetween(int id,Timestamp start, Timestamp end,Pageable pageable);
	
	
}
