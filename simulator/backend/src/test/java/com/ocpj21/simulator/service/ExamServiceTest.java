package com.ocpj21.simulator.service;

import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.model.ExamQuestion;
import com.ocpj21.simulator.model.Question;
import com.ocpj21.simulator.repository.ExamRepository;
import com.ocpj21.simulator.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private ExamService examService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startExam_ShouldCreateNewExamWithRandomQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            Question q = new Question();
            q.setId((long) i);
            allQuestions.add(q);
        }

        when(questionRepository.findAll()).thenReturn(allQuestions);
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exam result = examService.startExam();

        assertNotNull(result);
        assertEquals(50, result.getQuestions().size());
        assertNotNull(result.getStartTime());
        verify(questionRepository, times(1)).findAll();
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void submitExam_ShouldCalculateScoreAndMarkAsPassed() {
        Exam exam = new Exam();
        exam.setId(1L);

        Question q1 = new Question();
        q1.setId(1L);
        q1.setCorrectAnswers(Arrays.asList("A"));

        ExamQuestion eq1 = new ExamQuestion();
        eq1.setQuestion(q1);
        exam.getQuestions().add(eq1);

        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<Long, List<String>> answers = new HashMap<>();
        answers.put(1L, Arrays.asList("A"));

        Exam result = examService.submitExam(1L, answers);

        assertNotNull(result.getEndTime());
        assertEquals(1, result.getScore());
        assertFalse(result.getPassed()); // 1/50 is not passing
        assertTrue(eq1.getIsCorrect());
    }

    @Test
    void getExam_ShouldReturnExamById() {
        Exam exam = new Exam();
        exam.setId(1L);
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        Exam result = examService.getExam(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getExamResult_ShouldReturnMappedDTO() {
        Exam exam = new Exam();
        exam.setId(1L);
        exam.setStartTime(LocalDateTime.now());

        Question q = new Question();
        q.setId(1L);
        q.setChapter("ch01");
        q.setCorrectAnswers(Arrays.asList("A"));

        com.ocpj21.simulator.model.Option o = new com.ocpj21.simulator.model.Option();
        o.setId(1L);
        o.setLabel("A");
        o.setText("Option A");
        q.setOptions(Arrays.asList(o));

        ExamQuestion eq = new ExamQuestion();
        eq.setId(1L);
        eq.setQuestion(q);
        eq.setSelectedOptions(Arrays.asList("A"));
        eq.setIsCorrect(true);

        exam.setQuestions(Arrays.asList(eq));

        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        com.ocpj21.simulator.dto.ExamResultDTO result = examService.getExamResult(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getQuestions().size());
        assertEquals("Option A", result.getQuestions().get(0).getQuestion().getOptions().get(0).getText());
    }

    @Test
    void modelTests() {
        // Simple tests to cover Lombok generated code in models
        Question q = new Question();
        q.setId(1L);
        q.setText("Test");
        assertEquals(1L, q.getId());
        assertEquals("Test", q.getText());
        assertNotNull(q.getCorrectAnswers());

        Exam e = new Exam();
        e.setId(1L);
        assertEquals(1L, e.getId());

        ExamQuestion eq = new ExamQuestion();
        eq.setId(1L);
        assertEquals(1L, eq.getId());

        com.ocpj21.simulator.model.Option o = new com.ocpj21.simulator.model.Option();
        o.setId(1L);
        assertEquals(1L, o.getId());
    }
}
