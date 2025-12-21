package com.ocpj21.simulator.model;

import lombok.Data;
import com.google.cloud.firestore.annotation.DocumentId;
import java.util.List;
import java.util.ArrayList;

/**
 * Model representing a certification exam question.
 * Contains the question text, options, correct answers, and explanation.
 */
@Data
public class Question {
    @DocumentId
    private String id;

    private String chapter;
    private Integer questionNumber;
    private String text;
    private String codeSnippet;
    private List<Option> options = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>(); // e.g., ["A", "C"]
    private String explanation;
}
