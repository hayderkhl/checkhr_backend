package com.example.checkhrbackend_v2.controller;


import com.example.checkhrbackend_v2.DTO.AuthRequest;
import com.example.checkhrbackend_v2.model.Role;
import com.example.checkhrbackend_v2.model.User;
import com.example.checkhrbackend_v2.repository.UserRepository;
import com.example.checkhrbackend_v2.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;


    private String uploadPath="C:Users/hayde/My-Github/checkHrBackend_V2/utils/images";

    @PostMapping(value = "/addUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("birthdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdate,
            @RequestParam("age") Integer age,
            @RequestParam("phonenumber") String phonenumber,
            @RequestParam("education") String education,
            @RequestParam("address") String address,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "password_reset_token", required = false) String passwordResetToken) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.valueOf(role));
        user.setBirthdate(birthdate);
        user.setAge(age);
        user.setPhonenumber(phonenumber);
        user.setEducation(education);
        user.setAddress(address);
        user.setFullName(fullName);

        if (photo != null && !photo.isEmpty()) {
            try {
                // Generate a unique filename to avoid overwriting
                String filename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
                Path filePath = Paths.get(uploadPath, filename);

                // Create directories if they do not exist
                Files.createDirectories(filePath.getParent());

                // Save the file to the specified directory
                Files.write(filePath, photo.getBytes());

                // Set the filename in the user object (you can save the filename or the path)
                user.setPhoto(filename); // Assuming user.setPhoto expects a String
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        user.setPasswordResetToken(passwordResetToken);

        User registeredUser = authService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        String token = this.authService.login(authRequest);
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        User user = userRepository.findByUsername(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found with email: " + email);
        }

        // Generate a password reset token
        String token = authService.generatePasswordResetToken();
        // Save this token in the database with user association (omitted here for brevity)
        user.setPasswordResetToken(token);
        userRepository.save(user);
        // Send password reset email
        authService.sendPasswordResetEmail(user, token);

        return ResponseEntity.ok("Password reset instructions sent to " + email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

