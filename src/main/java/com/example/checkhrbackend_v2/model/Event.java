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
public class Event {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private  Long   event_id;

    @Column
    private String event_name;

    @Column
    private Date date;

    @Column
    private String content;
    @Column
    private String objective;
}
