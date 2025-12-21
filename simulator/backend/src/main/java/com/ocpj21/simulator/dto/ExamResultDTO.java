package com.ocpj21.simulator.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamResultDTO {
    private String id;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private Integer score;
    private Boolean passed;
    private List<ExamQuestionDTO> questions;
}
