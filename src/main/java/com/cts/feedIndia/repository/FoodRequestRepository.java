package com.cts.feedIndia.repository;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.FoodRequest;


@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest,Integer> {
	@Query("SELECT f FROM FoodRequest f WHERE f.city = :city AND f.pickupDate >= CURRENT_DATE AND f.fullfilled = false")
	public Page<FoodRequest> findByCity(@Param("city") String city, Pageable pageable);

	public Page<FoodRequest> findByUserId(int userId,Pageable pageable);
	
	@Query("SELECT f FROM FoodRequest f WHERE f.city = :city AND f.pickupDate = :date AND f.fullfilled = false")
	public Page<FoodRequest> findByCityDate( @Param("city")String city, @Param("date") Date date,Pageable pageable);
	
	
	@Query("SELECT f FROM FoodRequest f WHERE f.city = :city AND f.foodType = :type AND f.fullfilled = false")
	public Page<FoodRequest> findByCityAndType(@Param("city") String city,@Param("type") FoodType type,Pageable pageable);
	
	
	@Query("SELECT f FROM FoodRequest f WHERE f.city = :city AND f.pickupDate = :date AND f.foodType = :type AND f.fullfilled = false")
	public Page<FoodRequest> findByCityDateType(@Param("city") String city,@Param("date") Date date,@Param("type") FoodType type,Pageable pageable);
}

