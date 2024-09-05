package com.cts.feedIndia.security.jwt;

import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.services.impl.UserServiceImpl;
import com.cts.feedIndia.services.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cts.feedIndia.security.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserServiceImpl userService;

    public User userWithRefreshToken(String refreshToken) {
        return userService.findUserByRefreshToken(refreshToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = null;
        String refreshJwtToken = null;
        String username = null;

        // Get the cookies from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwt = cookie.getValue();
                    try {
                        username = jwtUtil.extractUsername(jwt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if (jwt == null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refreshJwtToken")) {
                        refreshJwtToken = cookie.getValue();
                        try {
                            username = jwtUtil.extractUsernameFromRefreshToken(refreshJwtToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);

            User existingUserWithRefreshToken = userWithRefreshToken(refreshJwtToken);

            if (jwt == null && jwtUtil.validateRefreshToken(refreshJwtToken, userDetails) && existingUserWithRefreshToken!=null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                final String newAuthToken = jwtUtil.generateToken(userDetails);
                final String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

                User updatedUser = userService.updateUserByRefreshToken(newRefreshToken, existingUserWithRefreshToken);

                // Create a new cookie for the JWT
                Cookie jwtCookie = new Cookie("jwt", newAuthToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setMaxAge(3600);
                jwtCookie.setPath("/");
                jwtCookie.setSecure(true);

                // Create a new cookie for the refresh token
                Cookie refreshTokenCookie = new Cookie("refreshJwtToken", newRefreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setMaxAge(86400);
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setSecure(true);

                // Add the cookies to the response
                response.addCookie(jwtCookie);
                response.addCookie(refreshTokenCookie);
            }

            if (jwt != null && jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
