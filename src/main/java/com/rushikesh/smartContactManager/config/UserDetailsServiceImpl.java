package com.rushikesh.smartContactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rushikesh.smartContactManager.dao.UserRepository;
import com.rushikesh.smartContactManager.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.getUserByEmail(email);

        if (user == null) {

            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);

    }
}
