package com.smarttravel.smarttravel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;


    // Default constructor
    public User() {
    }


    // Constructor
    public User(String name, String email, String password) {

        this.name = name;
        this.email = email;
        this.password = password;
    }


    // Get ID
    public Long getId() {
        return id;
    }


    // Set ID
    public void setId(Long id) {
        this.id = id;
    }


    // Get Name
    public String getName() {
        return name;
    }


    // Set Name
    public void setName(String name) {
        this.name = name;
    }


    // Get Email
    public String getEmail() {
        return email;
    }


    // Set Email
    public void setEmail(String email) {
        this.email = email;
    }


    // Get Password
    public String getPassword() {
        return password;
    }


    // Set Password
    public void setPassword(String password) {
        this.password = password;
    }
}