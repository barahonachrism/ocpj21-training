package com.ocpj21.simulator.service;

import com.ocpj21.simulator.model.Question;
import com.ocpj21.simulator.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarkdownParserServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    private MarkdownParserService markdownParserService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        markdownParserService = new MarkdownParserService(questionRepository, tempDir.toString());
    }

    @Test
    void loadQuestions_ShouldParseAndSaveQuestions() throws IOException {
        String questionContent = "## Practice Questions\n\n" +
                "**1. What is Java?**\n\n" +
                "**A) A programming language**\n" +
                "**B) A coffee brand**\n";

        String answerContent = "**1. The correct answer is A.**\n\n" +
                "**Explanation: Java is a high-level programming language.**\n";

        Files.writeString(tempDir.resolve("ch01.md"), questionContent);
        Files.writeString(tempDir.resolve("ch01a.md"), answerContent);

        // Mock count to 0 so it runs
        when(questionRepository.count()).thenReturn(0L);

        markdownParserService.loadQuestions();

        ArgumentCaptor<List<Question>> captor = ArgumentCaptor.forClass(List.class);
        verify(questionRepository, atLeastOnce()).saveAll(captor.capture());

        List<Question> savedQuestions = captor.getValue();
        assertFalse(savedQuestions.isEmpty());
        Question q = savedQuestions.get(0);
        assertEquals("ch01", q.getChapter());
        assertEquals("What is Java?", q.getText());
        assertEquals(2, q.getOptions().size());
        assertEquals(List.of("A"), q.getCorrectAnswers());
        assertEquals("Java is a high-level programming language.", q.getExplanation());
    }
}
