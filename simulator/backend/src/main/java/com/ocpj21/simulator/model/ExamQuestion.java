package com.ocpj21.simulator.model;

import lombok.Data;
import com.google.cloud.firestore.annotation.DocumentId;
import java.util.List;
import java.util.ArrayList;

/**
 * Model representing a specific question within an exam session.
 * Stores the user's selected options and whether the answer was correct.
 */
@Data
public class ExamQuestion {
    @DocumentId
    private String id;

    private Question question;
    private List<String> selectedOptions = new ArrayList<>();
    private Boolean isCorrect;
}
