package com.cts.feedIndia.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
public class CsrfController {

    /**
     * Endpoint to retrieve the CSRF token.
     *
     * @param request the HttpServletRequest object
     * @return the CSRF token wrapped in a ResponseEntity
     */
    @GetMapping("/csrf-token")
    public ResponseEntity<Map<String, String>> getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve the CSRF token from the request attributes
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // add csrf token to response header
        response.addHeader("X-CSRF-TOKEN", csrfToken.getToken());

        // Create a response body with the CSRF token
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("csrfToken", csrfToken.getToken());

        System.out.println("CSRF Token Header: " + response.getHeader("X-CSRF-TOKEN"));

        // Return the response entity with the CSRF token and HTTP status
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
