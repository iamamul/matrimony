package com.matrimony.hindu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Entity
@Data
public class UserDashBoard {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link back to NewUser
    @OneToOne
    @JoinColumn(name = "new_user_id", referencedColumnName = "id")
    private NewUser newUser;

    // Basic Additional Info
    private LocalDate dob;

    @Lob
//    @Column(name = "photo", columnDefinition = "BYTEA")
    @Column(name = "photo")
    @JsonIgnore
    private byte[] photo;

    // Education Info
    private String degree;
    private String institute;
    private String linkedInProfile;

    // Physical Attributes
    private double height; // in feet
    private double weight; // in kg
    private String hairColor;
    private String faceColor;
    private String bloodGroup;

    // Present Address
    private String country;
    private String state;
    private String city;
    private String postalCode;

    // Resident Information
    private String currentAddress;
    private String permanentAddress;

    // Career
    private String designation;
    private String company;
    private String companyCountry;
    private String companyState;
    private String companyCity;

    // Other Info
    private String languagesKnown;
    private String interests;

    // Family Information
    private String fatherName;
    private String motherName;
    private String siblings;
    private boolean disability;
    private String salary; // Optional package info
}
