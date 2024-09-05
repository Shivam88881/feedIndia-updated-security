package com.cts.feedIndia.services.service;

import java.util.List;

import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;


public interface UserService {
	
	public User addUser(User user) throws AlreadyExistException;
	
	public User findUserById(int userId) throws NotFoundException;
	
	public User findUserByEmail(String email) throws NotFoundException;
	
	public List<User> findUserByRole(String role) throws NotFoundException;
	
	public User findUserByRefreshToken(String refreshToken);
	
	public User updateUserById(int userId,User user) throws NotFoundException;

	public User updateUserByRefreshToken(String refreshToken,User user);

	public boolean deleteUserById(int userId) throws NotFoundException;
}
