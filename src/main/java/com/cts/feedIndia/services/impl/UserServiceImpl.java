package com.cts.feedIndia.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.cts.feedIndia.entity.Role;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.RoleRepository;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User addUser(User user) throws AlreadyExistException {
		log.info("Registering user");
		User existUser= repository.findByEmail(user.getEmail());
		if(existUser !=null) {
			log.info("User already exist with email "+ user.getEmail());
			throw new AlreadyExistException("User already exist");
		}
		User newUser = repository.save(user);
		if(newUser !=null) {
			log.info("User registration successful");
		}else {
			log.info("User registration failed");
		}
		return newUser;
	}

	@Override
	public User findUserById(int userId) throws NotFoundException {
		log.info("Finding user with id = "+userId);
		Optional<User> user=repository.findById(userId);
		if(user.isPresent()) {
			log.debug("User found "+user.get());
			return user.get();
		}else {
			log.info("No user with id = "+userId);
			throw new NotFoundException("No user with id = "+userId);
		}
		
	}

	@Override
	public List<User> findUserByRole(String roleName) throws NotFoundException {
		log.info("Finding user with role = "+roleName);
		Role role = roleRepository.findByRole(roleName);
		if(role!=null) {
			List<User> users = repository.findByRoleId(role.getId());
			log.info("Users found with role = "+role);
			return users;
		}else {
			throw new NotFoundException("No role exist with given role name");
		}
		
	}

	@Override
	public User findUserByRefreshToken(String refreshToken) {
		log.info("Finding user with refresh token = "+refreshToken);
		Optional<User> user =repository.findByRefreshTokenAndRefreshTokenExpireAfter(refreshToken, new Timestamp(System.currentTimeMillis()));
		if(user.isPresent()) {
			log.debug("User found "+user.get());
			return user.get();
		}else {
			log.info("No user with refresh token = "+refreshToken);
		}
		return null;
	}

	@Override
	public User updateUserById(int userId,User newUser) throws NotFoundException {
//		System.out.println("................................................................."+newUser.getEmail()+"........."+newUser.getUserDetail());
		Optional<User> user = repository.findById(userId);
		if(user.isPresent()) {
			log.debug("User found "+user.get());
			User oldUser =user.get();
			if(newUser.getName() != null) {
	            oldUser.setName(newUser.getName());
	        }
	        if(newUser.getEmail() != null) {
	            oldUser.setEmail(newUser.getEmail());
	        }
	        if(newUser.getDob() != null) {
	            oldUser.setDob(newUser.getDob());
	        }
	        if(newUser.getCity() != null) {
	            oldUser.setCity(newUser.getCity());
	        }
	        if(newUser.getRole() != null) {
	        	Role role = newUser.getRole();
	        	if (role != null && role.getId() ==null) {
	                role = roleRepository.save(role);
	                oldUser.setRole(role);
	            }else {
	            	
		        	Role newRole = roleRepository.findById(newUser.getRole().getId()).get();
		            oldUser.setRole(newRole);
	            }
	        }
	        if(newUser.getVerified() != null) {
	        	oldUser.setVerified(newUser.getVerified());
	        }
	        if(newUser.getEmailVerified() != null) {
	        	oldUser.setEmailVerified(newUser.getEmailVerified());
	        }
	        if(newUser.getPhone() != null) {
	            oldUser.setPhone(newUser.getPhone());
	        }
	        if(newUser.getAvatar() != null) {
	            oldUser.setAvatar(newUser.getAvatar());
	        }
	        if(newUser.getUserDetail() != null) {
	            oldUser.setUserDetail(newUser.getUserDetail());
	        }
	        if(newUser.getRefreshToken() != null) {
	        	oldUser.setRefreshToken(newUser.getRefreshToken());
	        }
	        repository.save(oldUser);
	        log.info("User updated");
			return oldUser;
		}else {
			log.info("No user exist with id = "+userId);
			throw new NotFoundException("No user exist with id = "+userId);
		}
		
	}

	@Override
	public User updateUserByRefreshToken(String refreshToken, User user) {
		user.setRefreshToken(refreshToken);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		timestamp.setHours(timestamp.getHours() + 24);
		user.setRefreshTokenExpire(timestamp);
		repository.save(user);
		return user;
	}

	@Override
	public boolean deleteUserById(int userId)throws NotFoundException {

		Optional<User> user = repository.findById(userId);
		log.info("Deteting user by id");
		if(user.isPresent()) {
			repository.deleteById(userId);
			log.info("User deleted successfuly");
			return true;
		}else {
			log.info("No user found with id = "+userId);
			throw new NotFoundException("No user found with id = "+userId);
		}
		
	}

	@Override
	public User findUserByEmail(String email) throws NotFoundException {
		log.info("Finding user by id");
		User user = repository.findByEmail(email);
		log.debug("User found {"+user+"}");
		if(user != null) {
			return user;
		}
		throw new NotFoundException("No user exist with given email");
	}

	

}
