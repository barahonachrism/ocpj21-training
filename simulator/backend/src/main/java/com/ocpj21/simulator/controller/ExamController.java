package com.ocpj21.simulator.controller;

import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*") // Allow all origins for development
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/start")
    public ResponseEntity<Exam> startExam() {
        return ResponseEntity.ok(examService.startExam());
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Exam> submitExam(@PathVariable Long id, @RequestBody Map<Long, List<String>> answers) {
        return ResponseEntity.ok(examService.submitExam(id, answers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.ocpj21.simulator.dto.ExamResultDTO> getExam(@PathVariable Long id) {
        System.out.println("ExamController: getExam request for id: " + id);
        com.ocpj21.simulator.dto.ExamResultDTO exam = examService.getExamResult(id);
        System.out.println("ExamController: getExam retrieved exam for id: " + id);
        return ResponseEntity.ok(exam);
    }
}
