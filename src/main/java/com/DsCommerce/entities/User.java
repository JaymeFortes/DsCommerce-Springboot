package com.DsCommerce.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String password;
}
