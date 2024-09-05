package com.cts.feedIndia.controller;

import com.cts.feedIndia.entity.Role;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.RoleServiceImpl;
import com.cts.feedIndia.services.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private RoleServiceImpl roleService;
    
    @Autowired
    private UserRepository userRepository;
    

    @GetMapping("/get/{id}")
    public ResponseEntity<User> findUserById(@PathVariable int id) throws NotFoundException {
        User user = userService.findUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new NotFoundException("No User found with the provided ID");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable int id, @RequestBody User user) throws NotFoundException,BadRequestException {
        
    	User updatedUser = userService.updateUserById(id, user);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            throw new BadRequestException("Failed to update the User");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) throws NotFoundException,BadRequestException {
    	User user = userService.findUserById(id);
    	
        if (user != null) {
        	Role defaultRole = roleService.findDefaultRole(); // You need to implement this method
            user.setRole(defaultRole);
            userRepository.save(user);
        }else {
        	throw new BadRequestException("No User exist with given id");
        }
        boolean isRemoved = userService.deleteUserById(id);
        if (isRemoved) {
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            throw new BadRequestException("Failed to delete the User");
        }
    }

    @GetMapping("/email")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) throws NotFoundException {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new NotFoundException("No User found with the provided email");
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> findUserByRole(@PathVariable String role) throws NotFoundException {
        List<User> users = userService.findUserByRole(role);
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            throw new NotFoundException("No Users found with the provided role");
        }
    }


    
   

}

