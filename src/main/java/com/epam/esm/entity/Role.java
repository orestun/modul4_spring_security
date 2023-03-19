package com.epam.esm.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String role;
    private Long userId;
}
