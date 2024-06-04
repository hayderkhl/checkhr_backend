package com.example.checkhrbackend_v2.service;

import com.example.checkhrbackend_v2.DTO.AuthRequest;
import com.example.checkhrbackend_v2.config.JwtTokenUtil;
import com.example.checkhrbackend_v2.model.User;
import com.example.checkhrbackend_v2.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JavaMailSender javaMailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.javaMailSender = javaMailSender;
    }

    // Method to send password reset email
    public void sendPasswordResetEmail(User user, String token) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setFrom("haidarkahla27@gmail.com");
            helper.setTo(user.getUsername());
            helper.setSubject("Password Reset Request");

            String resetUrl = "http://localhost:4200/reset-password";
            String htmlMsg = "<p>Hello " + user.getFullName() + ",</p>"
                    + "<p>You have requested to reset your password. Please use the following token to reset your password:</p>"
                    + "<p><strong>" + token + "</strong></p>"
                    + "<p>Go to the following link to reset your password: <a href=\"" + resetUrl + "\">Reset Password</a></p>"
                    + "<p>If you did not request this, please ignore this email.</p>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }

    public String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }

    // Method to handle the password reset process
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        // Reset the password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null); // Optional: Clear the token after reset
        userRepository.save(user);
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login (AuthRequest authRequest){
        System.out.println(authRequest.getUsername() +" " + authRequest.getPassword());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Exception e){
            System.out.println("The exception : "+ e);
        }
        var user= userRepository.findUsersByUsername(authRequest.getUsername()).orElseThrow();
        System.out.println(user.toString());
        var jwtToken = jwtTokenUtil.generateToken(user.getFullName(),user.getRole(),user.getId_user());
        System.out.println("token" + jwtToken);
        return  jwtToken;
    }
}
