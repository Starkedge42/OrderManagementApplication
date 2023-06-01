package com.ordermanagement.authservice.controller;


import com.ordermanagement.authservice.dto.AuthRequest;
import com.ordermanagement.authservice.entity.UserInfo;
import com.ordermanagement.authservice.exceptions.UserNotFoundException;
import com.ordermanagement.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addUser(@RequestBody UserInfo user){
        return authService.saveUser(user);
    }

    @GetMapping("/users")
    public List<UserInfo> getAllUsers(){
        return authService.getUsers();
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest){
        // validate if user(present in AuthRequest) is found in our db with correct name and password

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.getUserName(), authRequest.getPassword()));

        if(authentication.isAuthenticated())
            return authService.generateToken(authRequest.getUserName());
        else
            throw new UserNotFoundException(authRequest.getUserName());
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token){
         authService.validateToken(token);
         return "Token is valid";
    }
}
