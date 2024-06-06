package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.User;
import com.example.checkhrbackend_v2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@CrossOrigin("*")
@RequiredArgsConstructor

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findEmployees() {
        List<User> employees = this.userService.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> findEmployeeById(@PathVariable Long id) {
        Optional<User> employee = this.userService.getUserById(id);
        if (employee.isPresent()) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
        this.userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    public ResponseEntity<User> updateEmployee(@RequestBody User user) throws ClassNotFoundException {
        User updatedUser = this.userService.updateEmployee(user);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/status/{id}")
    public User changeUserStatus(@PathVariable Long id , @RequestBody String statusEnum) throws ClassNotFoundException {
        return this.userService.updateUserStatus(statusEnum,id);
    }
    @PostMapping("/rate/{id}/{note}")
    public User changeUserRate(@PathVariable Long id , @PathVariable Integer note) throws ClassNotFoundException {
        return this.userService.updateUserRate(id,note);
    }

    @GetMapping("/photo/{userId}")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable("userId") Long userId) {
        // Assuming you have a method to get the user's information by userId
        Optional<User> user = userService.getUserById(userId);
        if (user == null || user.get().getPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = user.get().getPhoto(); // Assuming the filename is stored in the user object
        Path filePath = Paths.get("D:/Hayder/checkhr_backend/utils/images").resolve(filename).normalize();

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                byte[] data = Files.readAllBytes(filePath);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG); // Set the content type as image/png or image/jpeg based on your file type

                return ResponseEntity.ok().headers(headers).body(data);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
