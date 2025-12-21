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
            q.setId(String.valueOf(i));
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
        exam.setId("1");

        Question q1 = new Question();
        q1.setId("1");
        q1.setCorrectAnswers(Arrays.asList("A"));

        ExamQuestion eq1 = new ExamQuestion();
        eq1.setQuestion(q1);
        exam.getQuestions().add(eq1);

        when(examRepository.findById("1")).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, List<String>> answers = new HashMap<>();
        answers.put("1", Arrays.asList("A"));

        Exam result = examService.submitExam("1", answers);

        assertNotNull(result.getEndTime());
        assertEquals(1, result.getScore());
        assertFalse(result.getPassed()); // 1/50 is not passing
        assertTrue(eq1.getIsCorrect());
    }

    @Test
    void getExam_ShouldReturnExamById() {
        Exam exam = new Exam();
        exam.setId("1");
        when(examRepository.findById("1")).thenReturn(Optional.of(exam));

        Exam result = examService.getExam("1");

        assertEquals("1", result.getId());
    }

    @Test
    void getExamResult_ShouldReturnMappedDTO() {
        Exam exam = new Exam();
        exam.setId("1");
        exam.setStartTime(LocalDateTime.now());

        Question q = new Question();
        q.setId("1");
        q.setChapter("ch01");
        q.setCorrectAnswers(Arrays.asList("A"));

        com.ocpj21.simulator.model.Option o = new com.ocpj21.simulator.model.Option();
        o.setId("1");
        o.setLabel("A");
        o.setText("Option A");
        q.setOptions(Arrays.asList(o));

        ExamQuestion eq = new ExamQuestion();
        eq.setId("1");
        eq.setQuestion(q);
        eq.setSelectedOptions(Arrays.asList("A"));
        eq.setIsCorrect(true);

        exam.setQuestions(Arrays.asList(eq));

        when(examRepository.findById("1")).thenReturn(Optional.of(exam));

        com.ocpj21.simulator.dto.ExamResultDTO result = examService.getExamResult("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(1, result.getQuestions().size());
        assertEquals("Option A", result.getQuestions().get(0).getQuestion().getOptions().get(0).getText());
    }
}
