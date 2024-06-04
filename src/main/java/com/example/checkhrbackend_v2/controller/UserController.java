package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.User;
import com.example.checkhrbackend_v2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
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

    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getEmployeePhoto(@PathVariable Long id) {
        Optional<byte[]> photoOptional = Optional.ofNullable(userService.getEmployeePhotoUrl(id));

        if (photoOptional.isPresent()) {
            byte[] photo = photoOptional.get();
            if (photo.length > 0) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG based on actual image type
                        .body(photo);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
