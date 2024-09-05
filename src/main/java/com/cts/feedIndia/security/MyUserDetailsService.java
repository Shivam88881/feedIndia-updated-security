package com.cts.feedIndia.security;


import java.util.Collections;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.feedIndia.repository.UserRepository;



@Service
public class MyUserDetailsService implements UserDetailsService {

   @Autowired
   private UserRepository usersDao;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
	   com.cts.feedIndia.entity.User user = usersDao.findByEmail(username);
	   
       if(user==null) {
    	   throw new UsernameNotFoundException("no user exist with given email");
       }
       List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRole()));
       return new User(user.getEmail(), user.getPassword(), authorities);
   }
}
