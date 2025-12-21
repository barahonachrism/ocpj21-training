package com.ocpj21.simulator.model;

import lombok.Data;
import com.google.cloud.firestore.annotation.DocumentId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Model representing an exam session.
 * Tracks the start and end times, score, and the set of questions included in
 * the exam.
 */
@Data
public class Exam {
    @DocumentId
    private String id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer score; // Number of correct questions
    private Boolean passed;

    private List<ExamQuestion> questions = new ArrayList<>();
}
