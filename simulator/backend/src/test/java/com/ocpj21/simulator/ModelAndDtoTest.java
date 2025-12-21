package com.ocpj21.simulator;

import com.ocpj21.simulator.dto.*;
import com.ocpj21.simulator.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelAndDtoTest {

    @Test
    void testModels() {
        Question q = new Question();
        q.setId("1");
        q.setChapter("ch01");
        q.setQuestionNumber(1);
        q.setText("Text");
        q.setCodeSnippet("Code");
        q.setExplanation("Exp");
        q.setCorrectAnswers(new ArrayList<>(List.of("A")));
        q.setOptions(new ArrayList<>());

        assertEquals("1", q.getId());
        assertEquals("ch01", q.getChapter());
        assertEquals(1, q.getQuestionNumber());
        assertEquals("Text", q.getText());
        assertEquals("Code", q.getCodeSnippet());
        assertEquals("Exp", q.getExplanation());
        assertEquals(1, q.getCorrectAnswers().size());
        assertNotNull(q.getOptions());

        Exam e = new Exam();
        e.setId("1");
        e.setStartTime(LocalDateTime.now());
        e.setEndTime(LocalDateTime.now());
        e.setScore(50);
        e.setPassed(true);
        e.setQuestions(new ArrayList<>());

        assertEquals("1", e.getId());
        assertNotNull(e.getStartTime());
        assertNotNull(e.getEndTime());
        assertEquals(50, e.getScore());
        assertTrue(e.getPassed());
        assertNotNull(e.getQuestions());

        Option o = new Option();
        o.setId("1");
        o.setLabel("A");
        o.setText("Opt");
        assertEquals("1", o.getId());
        assertEquals("A", o.getLabel());
        assertEquals("Opt", o.getText());

        ExamQuestion eq = new ExamQuestion();
        eq.setId("1");
        eq.setQuestion(q);
        eq.setSelectedOptions(new ArrayList<>(List.of("A")));
        eq.setIsCorrect(true);
        assertEquals("1", eq.getId());
        assertEquals(q, eq.getQuestion());
        assertEquals(1, eq.getSelectedOptions().size());
        assertTrue(eq.getIsCorrect());
    }

    @Test
    void testDTOs() {
        QuestionDTO q = new QuestionDTO();
        q.setId("1");
        q.setChapter("ch01");
        q.setQuestionNumber(1);
        q.setText("Text");
        q.setCodeSnippet("Code");
        q.setExplanation("Exp");
        q.setCorrectAnswers(new ArrayList<>(List.of("A")));
        q.setOptions(new ArrayList<>());

        assertEquals("1", q.getId());
        assertEquals("ch01", q.getChapter());
        assertEquals(1, q.getQuestionNumber());
        assertEquals("Text", q.getText());
        assertEquals("Code", q.getCodeSnippet());
        assertEquals("Exp", q.getExplanation());
        assertEquals(1, q.getCorrectAnswers().size());
        assertNotNull(q.getOptions());

        ExamResultDTO e = new ExamResultDTO();
        e.setId("1");
        e.setStartTime(LocalDateTime.now());
        e.setEndTime(LocalDateTime.now());
        e.setScore(50);
        e.setPassed(true);
        e.setQuestions(new ArrayList<>());

        assertEquals("1", e.getId());
        assertNotNull(e.getStartTime());
        assertNotNull(e.getEndTime());
        assertEquals(50, e.getScore());
        assertTrue(e.getPassed());
        assertNotNull(e.getQuestions());

        OptionDTO o = new OptionDTO();
        o.setId("1");
        o.setLabel("A");
        o.setText("Opt");
        assertEquals("1", o.getId());
        assertEquals("A", o.getLabel());
        assertEquals("Opt", o.getText());

        ExamQuestionDTO eq = new ExamQuestionDTO();
        eq.setId("1");
        eq.setSelectedOptions(new ArrayList<>(List.of("A")));
        eq.setIsCorrect(true);
        eq.setQuestion(q);
        assertEquals("1", eq.getId());
        assertEquals(1, eq.getSelectedOptions().size());
        assertTrue(eq.getIsCorrect());
        assertEquals(q, eq.getQuestion());
    }
}
