package com.ocpj21.simulator.controller;

import com.ocpj21.simulator.dto.ExamResultDTO;
import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExamController.class)
class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void startExam_ShouldReturnNewExam() throws Exception {
        Exam exam = new Exam();
        exam.setId("1");
        when(examService.startExam()).thenReturn(exam);

        mockMvc.perform(post("/api/exam/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void submitExam_ShouldReturnUpdatedExam() throws Exception {
        Exam exam = new Exam();
        exam.setId("1");
        Map<String, List<String>> answers = new HashMap<>();
        answers.put("1", List.of("A"));

        when(examService.submitExam(eq("1"), any())).thenReturn(exam);

        mockMvc.perform(post("/api/exam/1/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(answers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void getExam_ShouldReturnExamResult() throws Exception {
        ExamResultDTO dto = new ExamResultDTO();
        dto.setId("1");
        when(examService.getExamResult("1")).thenReturn(dto);

        mockMvc.perform(get("/api/exam/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }
}
