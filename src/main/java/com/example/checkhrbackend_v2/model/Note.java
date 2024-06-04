package com.example.checkhrbackend_v2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Note {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id_note;

    @Column
    private String content;

    @Column
    private Date time;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
