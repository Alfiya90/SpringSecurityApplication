package com.example.springsecurity.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JacksonAnnotation;

@Entity
@Table(name = "user")
public class UserDAO {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private String username;
   @Column
    private String password;
    @Column
    private String role;

    public long getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
