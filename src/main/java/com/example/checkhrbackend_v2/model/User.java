package com.example.checkhrbackend_v2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")

public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id_user;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String FullName;

    @Column(nullable = false)
    private  String password;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String department="Marketing";
    @Column(nullable = false)
    private String status="Permanent";
    @Column
    private Date birthdate;
    @Column
    private Integer age;
    @Column
    private String phonenumber;
    @Column
    private String education;
    @Column
    private String address;
    @Column
    private Integer rate;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "password_reset_token")
    private String passwordResetToken;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Documents> documents;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Loan> loans;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Training> trainings;
    //    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    //  private List<Leave> leaves;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Leaves> lvs;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> notes;
    @Override
    public String toString() {
        return "User [username=" + username + ", email="  + "]";
    }
}
