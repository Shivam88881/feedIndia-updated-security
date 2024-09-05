package com.cts.feedIndia.controller;

import java.util.HashMap;
import java.util.Map;

import com.cts.feedIndia.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.entity.UserDto;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.security.AuthenticationRequest;
import com.cts.feedIndia.security.jwt.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 

 
@RestController
@RequestMapping("/v1/api")
public class LoginLogoutController {
 
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;
 
    @Autowired
    private JwtUtil jwtUtil;
 
    @Autowired
    private UserDetailsService userDetailsService;
 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse res) throws BadRequestException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            throw new BadRequestException("Inavlid username or password");
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final String refreshJwtToken = jwtUtil.generateRefreshToken(userDetails);


        User user = userRepository.findByEmail(authenticationRequest.getUsername());

        user = userService.updateUserByRefreshToken(refreshJwtToken, user);


        user = userRepository.save(user);
        // Create a new cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(3600);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);

        // Create a new cookie
        Cookie refreshJwtCookie = new Cookie("refreshJwtToken", refreshJwtToken);
        refreshJwtCookie.setHttpOnly(true);
        refreshJwtCookie.setMaxAge(86400);
        refreshJwtCookie.setPath("/");
        refreshJwtCookie.setSecure(true);
        
        // Add the cookie to the response
        res.addCookie(jwtCookie);
        res.addCookie(refreshJwtCookie);
        
        UserDto userDto = new UserDto();
        userDto.convertToDto(user);
        return ResponseEntity.ok(userDto);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    	Cookie jwtCookie = new Cookie("jwt", null);
        Cookie refreshJwtCookie = new Cookie("refreshJwtToken", null);

        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);

        refreshJwtCookie.setHttpOnly(true);
        refreshJwtCookie.setMaxAge(0);
        refreshJwtCookie.setPath("/");
        refreshJwtCookie.setSecure(true);

        response.addCookie(refreshJwtCookie);
        response.addCookie(jwtCookie);

        Map<String, String> resBody = new HashMap<>();
        resBody.put("message", "Logout successful");

        return ResponseEntity.ok(resBody);
    }
    
    
    @GetMapping("/load/user")
    public ResponseEntity<User> loadUser(HttpServletRequest request) throws BadRequestException{
    	String username=null;
    	String jwt = null;
        String refreshToken = null;
    	Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    // Invalidate the cookie
                	jwt = cookie.getValue();
                    try {
                    	username = jwtUtil.extractUsername(jwt);
                    }catch(Exception e) {
                    	throw new BadRequestException(e.getMessage());
                    }
                    break;
                }
            }

            if(jwt == null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refreshJwtToken")) {
                        // Invalidate the cookie
                        refreshToken = cookie.getValue();
                        try {
                            username = jwtUtil.extractUsername(refreshToken);
                        }catch(Exception e) {
                            throw new BadRequestException(e.getMessage());
                        }
                        break;
                    }
                }
            }
        }else {
        	throw new BadRequestException("Login to use all feature");
        }
        
        if(username != null) {
        	User user = userRepository.findByEmail(username);
        	return ResponseEntity.ok(user);
        }
    	
    	throw new BadRequestException("JWT token is invalid");
    }
}
