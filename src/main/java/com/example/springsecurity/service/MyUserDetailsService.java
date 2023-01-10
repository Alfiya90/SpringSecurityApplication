package com.example.springsecurity.service;

import com.example.springsecurity.dto.UserDTO;
import com.example.springsecurity.model.UserDAO;
import com.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userDAO;

    private PasswordEncoder bCryptEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> role = null;
        UserDAO user = userDAO.findByUsername(username);
        if(user !=null) {
            role = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
            return  new User(user.getUsername(), user.getPassword(), role);
        }

       /* if(username.equals("user")) {
            role = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            return new User("user", "$2a$10$PCVAk/4MzUApeOnXlOn1S.B2yufDSkgygcXjb/UxyMNS9KHz3JfNC", role);
        }
        if (username.equals("admin")) {
            role = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User ("admin", "$2a$10$xmNsK8qWZWAR6qr1aDsr3ehIjHoFSSe2SA8a6ZTIp7Y0tbp9avVWe", role);
        }*/
        throw new UsernameNotFoundException("User with username " + username + " not found");
    }

    public UserDAO save (UserDTO userDTO) {
        UserDAO newUser = new UserDAO();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(bCryptEncoder.encode(userDTO.getPassword()));
        newUser.setRole(userDTO.getRole());
        return userDAO.save(newUser);
    }
}
