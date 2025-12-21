package com.ocpj21.simulator.controller;

import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing exams.
 * Provides endpoints for starting an exam, submitting answers, and retrieving
 * results.
 */
@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*") // Allow all origins for development
public class ExamController {

    private final ExamService examService;

    /**
     * Constructs an ExamController with the necessary ExamService.
     *
     * @param examService the service for managing exams
     */
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * Endpoint to start a new exam.
     *
     * @return a ResponseEntity containing the newly created Exam
     */
    @PostMapping("/start")
    public ResponseEntity<Exam> startExam() {
        return ResponseEntity.ok(examService.startExam());
    }

    /**
     * Endpoint to submit an exam.
     *
     * @param id      the ID of the exam to submit
     * @param answers a map of question IDs to the list of selected option labels
     * @return a ResponseEntity containing the updated Exam with results
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<Exam> submitExam(@PathVariable Long id, @RequestBody Map<Long, List<String>> answers) {
        return ResponseEntity.ok(examService.submitExam(id, answers));
    }

    /**
     * Endpoint to retrieve the results of an exam.
     *
     * @param id the ID of the exam
     * @return a ResponseEntity containing the ExamResultDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.ocpj21.simulator.dto.ExamResultDTO> getExam(@PathVariable Long id) {
        System.out.println("ExamController: getExam request for id: " + id);
        com.ocpj21.simulator.dto.ExamResultDTO exam = examService.getExamResult(id);
        System.out.println("ExamController: getExam retrieved exam for id: " + id);
        return ResponseEntity.ok(exam);
    }
}
