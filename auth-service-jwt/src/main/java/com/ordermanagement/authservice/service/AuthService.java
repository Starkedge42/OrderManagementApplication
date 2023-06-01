package com.ordermanagement.authservice.service;


import com.ordermanagement.authservice.entity.UserInfo;
import com.ordermanagement.authservice.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserInfo userInfo){

        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userDetailsRepository.save(userInfo);
        return "User " + userInfo.getName() + " registered successfully.";
    }

    public String generateToken(String userName){
        return jwtService.generateToken(userName);
    }

    public void validateToken(String token){
        jwtService.validateToken(token);
    }

    public List<UserInfo> getUsers(){
        return userDetailsRepository.findAll();
    }
}
