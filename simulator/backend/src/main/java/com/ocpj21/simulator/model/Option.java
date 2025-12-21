package com.ocpj21.simulator.model;

import lombok.Data;
import com.google.cloud.firestore.annotation.DocumentId;

/**
 * Model representing an answer option for a question.
 */
@Data
public class Option {
    @DocumentId
    private String id;

    private String label; // A, B, C, D
    private String text;
}
