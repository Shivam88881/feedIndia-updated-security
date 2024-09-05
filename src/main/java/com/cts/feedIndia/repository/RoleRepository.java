package com.cts.feedIndia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.feedIndia.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
	public Role findByRole(String Role);
}
