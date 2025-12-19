package com.ocpj21.simulator.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chapter;
    private Integer questionNumber;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(columnDefinition = "TEXT")
    private String codeSnippet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private List<Option> options = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> correctAnswers = new ArrayList<>(); // e.g., ["A", "C"]

    @Column(columnDefinition = "TEXT")
    private String explanation;
}
