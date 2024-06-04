package com.example.checkhrbackend_v2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Loan {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id_request ;

    @Column
    private Date date ;

    @Column
    private Double amount ;

    @Column
    private Boolean status;
    @Column
    private String specification;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
