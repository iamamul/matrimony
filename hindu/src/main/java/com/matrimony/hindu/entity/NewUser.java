package com.matrimony.hindu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class NewUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String maritalStatus;
    private long mobileNumber;
    private String location;
    private String religion;
    private String motherTonge;
    private String password;

    @OneToOne(mappedBy = "newUser", cascade = CascadeType.ALL)
    @ToString.Exclude  // Prevents infinite recursion in toString
    private UserDashBoard userDashBoard;
}
