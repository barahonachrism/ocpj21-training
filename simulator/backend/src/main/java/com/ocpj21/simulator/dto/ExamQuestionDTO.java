package com.ocpj21.simulator.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExamQuestionDTO {
    private String id;
    private QuestionDTO question;
    private List<String> selectedOptions;
    private Boolean isCorrect;
}
