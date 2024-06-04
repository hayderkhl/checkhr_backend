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
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_request ;

    @Column
    private Date date ;
    @Column
    private String coursename;
    @Column
    private Integer numchapters;
    @Column
    private String coursesize;
    @Column
    private Boolean status = null;
    @Column
    private String specifications;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
