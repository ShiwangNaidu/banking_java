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
import com.ega.banking.services.UserService;
import com.ega.banking.util.JwtTokenUtil;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService service;

    @GetMapping("/get")
    public String getMsg() {
        return "hi";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateJwtToken(user.getFirstName());
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody User user) {
        return service.addUser(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity generateJwtToken(@RequestBody AuthRequest authRequest) throws Exception {
        // System.out.println("authentication email and password" +
        // authRequest.getEmail()+ authRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            // System.out.println("authentication" + authentication.isAuthenticated());
            if (authentication.isAuthenticated()) {
                // User user = (User) authentication.getPrincipal();
                return ResponseEntity.status(200).body(jwtUtil.generateJwtToken(authentication.getName()));
            } 
            else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User Found");
        } catch (Exception e) {
            //e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        

    }
}