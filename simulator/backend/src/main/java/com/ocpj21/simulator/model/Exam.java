package com.ocpj21.simulator.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer score; // Number of correct questions
    private Boolean passed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exam", fetch = FetchType.EAGER)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<ExamQuestion> questions = new ArrayList<>();
}
