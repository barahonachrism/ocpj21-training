package com.ocpj21.simulator.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

/**
 * Entity representing a specific question within an exam session.
 * Stores the user's selected options and whether the answer was correct.
 */
@Entity
@Data
public class ExamQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> selectedOptions = new ArrayList<>();

    private Boolean isCorrect;
}
