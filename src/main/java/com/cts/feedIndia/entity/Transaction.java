package com.cts.feedIndia.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;

import com.cts.feedIndia.constant.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="transaction")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="native")
	@GenericGenerator(name="native")
	@Column(name="id")
	private Integer id;
	
	@Column(name="amount")
	private Integer amount;
	
	@NonNull
	@Column(name="donation_date_time")
	private Timestamp donationDateTime;
	
	@NonNull
	@Column(name="status")
	private TransactionStatus status;
	
	@ManyToOne
    @JoinColumn(name = "donor_id")
    private User donor;
}
