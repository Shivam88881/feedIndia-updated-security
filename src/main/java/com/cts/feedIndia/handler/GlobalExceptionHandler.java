package com.cts.feedIndia.handler;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	Map<String,Object> response;
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String,Object>> notFoundExceptionhandler(Exception e){
		response=new HashMap<>();
		response.put("statusCode", HttpStatus.NOT_FOUND.value());
		response.put("message", e.getMessage());
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<Map<String,Object>> alreadyExistExceptionhandler(Exception e){
		response=new HashMap<>();
		response.put("statusCode", HttpStatus.CONFLICT.value());
		response.put("message", e.getMessage());
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String,Object>> badRequestExceptionhandler(Exception e){
		response=new HashMap<>();
		response.put("statusCode", HttpStatus.BAD_REQUEST.value());
		response.put("message", e.getMessage());
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
	}

}
