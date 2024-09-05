package com.cts.feedIndia.repository;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.feedIndia.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
	Optional<User> findByRefreshTokenAndRefreshTokenExpireAfter(String refereshToken, Timestamp currentTime);
	List<User> findByRoleId(int id);
	User findByEmail(String email);
}
