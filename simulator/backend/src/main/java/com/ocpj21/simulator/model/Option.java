package com.ocpj21.simulator.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label; // A, B, C, D

    @Column(columnDefinition = "TEXT")
    private String text;
}
