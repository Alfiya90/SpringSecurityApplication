package com.example.springsecurity.repository;

import com.example.springsecurity.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserDAO, Long> {
     UserDAO findByUsername(String username);
}
