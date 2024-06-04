package com.example.checkhrbackend_v2.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
