package com.ega.banking.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.ega.banking.entities.AuthRequest;
import com.ega.banking.entities.AuthResponse;
import com.ega.banking.entities.User;
import com.ega.banking.util.JwtTokenUtil;

@RestController
public class AuthApi {

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/get")
    public String getMsg() {
        return "hi";
    }

    @GetMapping("/users/getAuth")
    public String getMsg2() {
        return "hi2";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateJwtToken(user.getUsername());
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/authenticate")
    public String generateJwtToken(@RequestBody AuthRequest authRequest) {
        System.out.println("authentication email and password" + authRequest.getEmail()+ authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        System.out.println("authentication" + authentication.isAuthenticated());
        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return jwtUtil.generateJwtToken(user.getUsername());
        } else
            throw new UsernameNotFoundException("invalid user");
    }
}