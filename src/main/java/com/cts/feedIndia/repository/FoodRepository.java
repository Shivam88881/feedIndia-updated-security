package com.cts.feedIndia.repository;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.feedIndia.constant.FoodType;
import com.cts.feedIndia.entity.Food;


@Repository
public interface FoodRepository extends JpaRepository<Food,Integer> {
	@Query("SELECT f FROM Food f WHERE f.city = :city AND f.pickupDate >= CURRENT_DATE AND f.available = true")
	public Page<Food> findByCity(@Param("city") String city, Pageable pageable);
	
	
	@Query("SELECT f FROM Food f WHERE f.city = :city AND f.pickupDate = :date AND f.available = true")
	public Page<Food> findByCityDate(@Param("city") String city,@Param("date") Date date, Pageable pagable);
	
	
	@Query("SELECT f FROM Food f WHERE f.user.id = :userId ")
	public Page<Food> findByUserId( @Param("userId") int userId, Pageable pageable);
	
	
	@Query("SELECT f FROM Food f WHERE f.city = :city AND f.foodType = :type AND f.available = true")
	public Page<Food> findByCityAndType(@Param("city") String city,  @Param("type") FoodType type, Pageable pagable);
	
	
	@Query("SELECT f FROM Food f WHERE f.city = :city AND f.pickupDate = :date AND f.foodType = :type AND f.available = true")
	public Page<Food> findByCityDateType(@Param("city") String city,@Param("date") Date date,@Param("type") FoodType type, Pageable pagable);
}
