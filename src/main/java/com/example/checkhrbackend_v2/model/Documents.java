package com.example.checkhrbackend_v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Documents {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id_doc;

    @Column
    private String docname ;

    @Column
    private  Double docsize;
    @Column
    private Date date;
    @Column
    private String path;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;
}
