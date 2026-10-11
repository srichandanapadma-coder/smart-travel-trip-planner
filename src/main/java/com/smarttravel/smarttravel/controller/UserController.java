package com.smarttravel.smarttravel.controller;

import com.smarttravel.smarttravel.dto.LoginRequest;
import com.smarttravel.smarttravel.dto.LoginResponse;
import com.smarttravel.smarttravel.entity.User;
import com.smarttravel.smarttravel.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "api is working";
    }
    @GetMapping("/profile")
public ResponseEntity<String> profile() {
    return ResponseEntity.ok("You are authenticated!");
}

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody User user) {

        User registeredUser = userService.registerUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody LoginRequest request) {

        LoginResponse response =
                userService.loginUser(request);

        return ResponseEntity.ok(response);
    }
}