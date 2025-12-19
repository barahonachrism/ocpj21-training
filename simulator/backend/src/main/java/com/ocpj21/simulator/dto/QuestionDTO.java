package com.ocpj21.simulator.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String chapter;
    private Integer questionNumber;
    private String text;
    private String codeSnippet;
    private List<OptionDTO> options;
    private List<String> correctAnswers;
    private String explanation;
}
