package com.cts.feedIndia.entity;


import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
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
@Table(name="users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="native")
	@GenericGenerator(name="native")
	@Column(name="id")
	private Integer id;
	
	@NonNull
	@Column(name="name")
	private String name;
	
	@NonNull
	@Column(name="email", unique=true)
	private String email;
	
	@NonNull
	@Column(name="password")
	private String password;
	
	
	@Column(name="dob")
	private String dob;
	
	@NonNull
	@Column(name="city")
	private String city;
	
	
	@Column(name="verified")
	private Boolean verified;
	
	@Column(name="email_verified")
	private Boolean emailVerified;
	
	
	@NonNull
	@Column(name="phone")
	private String phone;
	
	@Column(name="avatar")
	private String avatar;
	
	@Column(name="user_detail")
	private String userDetail;
	
	@Column(name="refresh_token")
	private String refreshToken;
	
	@Column(name="refresh_token_expire")
	private Timestamp refreshTokenExpire;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable=false)
    private Role role;
}
