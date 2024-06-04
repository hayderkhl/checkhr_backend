package com.example.checkhrbackend_v2.service;

import com.example.checkhrbackend_v2.model.Role;
import com.example.checkhrbackend_v2.model.User;
import com.example.checkhrbackend_v2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user){
        return this.userRepository.save(user);
    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }
    public Optional<User> getUserById(Long id ){
        return this.userRepository.findById(id);
    }
    public void deleteUserById(Long id){
        this.userRepository.deleteById(id);
    }

    public List<User> findEmployees(){
        return this.userRepository.findUsersByRole(Role.EMPLOYEE);
    }

    public  User updateEmployee(User user) throws ClassNotFoundException {
        Optional<User> us = getUserById(user.getId_user());
        if(!us.isPresent()){
            throw new ClassNotFoundException("Cannot find User with this data ");
        }
        return this.userRepository.save(user);
    }
    public User updateUserStatus(String statusEnum,Long id) throws ClassNotFoundException{
        Optional<User> user = this.userRepository.findById(id);
        System.out.print(user.get().toString());
        if(user.isPresent()){
            user.get().setStatus(statusEnum);
            return this.userRepository.save(user.get());
        }else{
            throw new ClassNotFoundException("User Not found");
        }
    }
    public User updateUserRate(Long id , Integer note) throws ClassNotFoundException{
        Optional<User> is = this.userRepository.findById(id);
        if(is.isPresent()){
            is.get().setRate(note);
            return this.userRepository.save(is.get());
        }else{
            throw new ClassNotFoundException("Cannot find user");
        }
    }

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public byte[] getEmployeePhotoUrl(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            byte[] photo = user.get().getPhoto(); // Assuming `getPhoto()` returns the photo data as a byte array
            if (photo != null && photo.length > 0) {
                logger.info("Photo fetched successfully for user id: " + id);
                return photo;
            } else {
                logger.warning("Photo is empty for user id: " + id);
            }
        } else {
            logger.warning("User not found with id: " + id);
        }
        return null;
    }

    private byte[] fetchImageFromUrl(String photoUrl) throws IOException {
        URL url = new URL(photoUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to fetch image, HTTP response code: " + connection.getResponseCode());
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }

}
