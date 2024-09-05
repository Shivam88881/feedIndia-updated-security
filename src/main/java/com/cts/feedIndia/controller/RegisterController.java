package com.cts.feedIndia.controller;

import com.cts.feedIndia.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.feedIndia.entity.Role;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.entity.UserDto;
import com.cts.feedIndia.repository.RoleRepository;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.security.jwt.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

 
@RestController
@RequestMapping("/v1/api/register")
public class RegisterController {
 
	
    @Autowired
    private UserRepository usersDao;
    
    @Autowired
    private RoleRepository roleRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserServiceImpl userService;
 
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user,HttpServletResponse res) {
        if (usersDao.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = user.getRole();
        if (role != null && role.getId() ==null) {
        	role = roleRepository.save(role);
            user.setRole(role);
        }else {
        	user.setRole(roleRepository.findById(role.getId()).get());
        }
        User newUser = null;
        
        try {
        	newUser = usersDao.save(user);
        }finally {
        	final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            
            final String jwt = jwtUtil.generateToken(userDetails);
            final String refreshJwtToken = jwtUtil.generateRefreshToken(userDetails);

            newUser = userService.updateUserByRefreshToken(refreshJwtToken, newUser);

            newUser = usersDao.save(newUser);

            
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(3600);
            jwtCookie.setPath("/");
            jwtCookie.setSecure(true);

            Cookie refreshJwtCookie = new Cookie("refreshJwtToken", refreshJwtToken);
            refreshJwtCookie.setHttpOnly(true);
            refreshJwtCookie.setMaxAge(86400);
            refreshJwtCookie.setPath("/");
            refreshJwtCookie.setSecure(true);

            res.addCookie(refreshJwtCookie);
            res.addCookie(jwtCookie);
        }
        
        UserDto userDto = new UserDto();
        userDto.convertToDto(newUser);
        return ResponseEntity.ok(userDto);
    }
}
