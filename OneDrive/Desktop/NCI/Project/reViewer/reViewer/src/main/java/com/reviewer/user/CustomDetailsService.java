package com.reviewer.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        User user = repo.findByEmailAddress(emailAddress);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
