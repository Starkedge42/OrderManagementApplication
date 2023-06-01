package com.ordermanagement.authservice.service;


import com.ordermanagement.authservice.exceptions.UserNotFoundException;
import com.ordermanagement.authservice.repository.UserDetailsRepository;
import com.ordermanagement.authservice.entity.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userDetailsRepository.findByName(username)
                .map(userInfo -> new CustomUserDetails(userInfo))
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not present in database"));
    }
}
