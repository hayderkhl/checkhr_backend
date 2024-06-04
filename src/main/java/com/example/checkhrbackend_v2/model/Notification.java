package com.example.checkhrbackend_v2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String Title;

    private String message;

    private Boolean isRead = false;
}
