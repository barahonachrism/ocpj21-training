package com.ocpj21.simulator.service;

import com.ocpj21.simulator.model.Option;
import com.ocpj21.simulator.model.Question;
import com.ocpj21.simulator.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for parsing exam questions and answers from Markdown files.
 * Automatically loads questions on startup if the database is empty.
 */
@Service
public class MarkdownParserService {

    private final QuestionRepository questionRepository;
    private final String bookPath;

    /**
     * Constructs a MarkdownParserService.
     *
     * @param questionRepository the repository for managing Question entities
     * @param bookPath           the file system path where the book's Markdown
     *                           files are located
     */
    public MarkdownParserService(QuestionRepository questionRepository,
            @Value("${book.path:/Users/christian.barahona/Documents/ocpj21-book}") String bookPath) {
        this.questionRepository = questionRepository;
        this.bookPath = bookPath;
    }

    /**
     * Post-construct hook that triggers the loading of questions from the book
     * path.
     * Only loads questions if the database is currently empty.
     */
    @PostConstruct
    public void loadQuestions() {
        if (questionRepository.count() > 0) {
            return; // Already loaded
        }

        System.out.println("Loading questions from: " + bookPath);

        for (int i = 1; i <= 14; i++) {
            String chapter = String.format("ch%02d", i);
            try {
                processChapter(chapter);
            } catch (Exception e) {
                System.err.println("Error processing chapter " + chapter + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes a single chapter by reading its question and answer Markdown files.
     *
     * @param chapterName the name of the chapter (e.g., "ch01")
     * @throws IOException if there's an error reading the files
     */
    private void processChapter(String chapterName) throws IOException {
        Path questionFile = Paths.get(bookPath, chapterName + ".md");
        Path answerFile = Paths.get(bookPath, chapterName + "a.md");

        if (!Files.exists(questionFile) || !Files.exists(answerFile)) {
            System.out.println("Files not found for " + chapterName);
            return;
        }

        String questionContent = Files.readString(questionFile);
        String answerContent = Files.readString(answerFile);

        List<Question> questions = parseQuestions(questionContent, chapterName);
        parseAnswers(answerContent, questions);

        questionRepository.saveAll(questions);
        System.out.println("Saved " + questions.size() + " questions for " + chapterName);
    }

    /**
     * Parses the question content from a Markdown string.
     *
     * @param content     the Markdown content of the chapter
     * @param chapterName the name of the chapter
     * @return a list of parsed Question entities
     */
    private List<Question> parseQuestions(String content, String chapterName) {
        List<Question> questions = new ArrayList<>();

        // Find "## Practice Questions"
        int startIndex = content.indexOf("## Practice Questions");
        if (startIndex == -1)
            return questions;

        String practiceSection = content.substring(startIndex);

        // Regex to split by question number "**N. "
        // We look for ** followed by a number, a dot, and a space
        Pattern questionPattern = Pattern.compile("\\*\\*(\\d+)\\.\\s+(.*?)(?=\\*\\*\\d+\\.|\\Z)", Pattern.DOTALL);
        Matcher matcher = questionPattern.matcher(practiceSection);

        while (matcher.find()) {
            Question q = new Question();
            q.setChapter(chapterName);
            q.setQuestionNumber(Integer.parseInt(matcher.group(1)));

            String fullText = matcher.group(2).trim();

            // Extract Options
            // Options usually start with **A)** or **A.**
            List<Option> options = new ArrayList<>();
            // Improved regex: remove \\s+ requirement after ) and use lazy quantifier
            Pattern optionPattern = Pattern.compile("\\*\\*([A-Z])\\)(.*?)(?=\\*\\*[A-Z]\\)|\\Z)", Pattern.DOTALL);
            Matcher optionMatcher = optionPattern.matcher(fullText);

            // The text before the first option is the question text
            int firstOptionIndex = -1;
            if (optionMatcher.find()) {
                firstOptionIndex = optionMatcher.start();
                // Reset matcher
                optionMatcher.reset();
            }

            if (firstOptionIndex != -1) {
                String text = fullText.substring(0, firstOptionIndex).trim();
                if (text.endsWith("**")) {
                    text = text.substring(0, text.length() - 2).trim();
                }
                q.setText(text);
                while (optionMatcher.find()) {
                    Option o = new Option();
                    o.setLabel(optionMatcher.group(1));
                    o.setText(optionMatcher.group(2).trim());
                    options.add(o);
                }
            } else {
                String text = fullText;
                if (text.endsWith("**")) {
                    text = text.substring(0, text.length() - 2).trim();
                }
                q.setText(text);
            }

            q.setOptions(options);
            questions.add(q);
        }

        return questions;
    }

    /**
     * Parses the answers and explanations from a Markdown string and updates the
     * provided questions.
     *
     * @param content   the Markdown content containing the answers
     * @param questions the list of questions to update with correct answers and
     *                  explanations
     */
    private void parseAnswers(String content, List<Question> questions) {
        // Regex for answers: "**N. The correct answer is X.**" or "**N. The correct
        // answers are X and Y.**"
        // And Explanation

        for (Question q : questions) {
            Pattern answerPattern = Pattern.compile(
                    "\\*\\*" + q.getQuestionNumber() + "\\.\\s+The correct answer[s]? (is|are) (.*?)\\.\\*\\*",
                    Pattern.DOTALL);
            Matcher matcher = answerPattern.matcher(content);

            if (matcher.find()) {
                String correctStr = matcher.group(2);
                // Parse correct options (A, B, C...)
                // Example: "B" or "A and E" or "A, B, and C"
                Pattern labelPattern = Pattern.compile("([A-Z])");
                Matcher labelMatcher = labelPattern.matcher(correctStr);
                while (labelMatcher.find()) {
                    q.getCorrectAnswers().add(labelMatcher.group(1));
                }

                // Extract Explanation
                // It usually follows the answer line and goes until the next "**N." or end of
                // file
                int explanationStart = matcher.end();

                // Find next question start
                Pattern nextQuestionPattern = Pattern.compile("\\*\\*" + (q.getQuestionNumber() + 1) + "\\.");
                Matcher nextMatcher = nextQuestionPattern.matcher(content);

                int explanationEnd = content.length();
                if (nextMatcher.find(explanationStart)) {
                    explanationEnd = nextMatcher.start();
                }

                String explanation = content.substring(explanationStart, explanationEnd).trim();
                // Remove "**Explanation:**" or "**Explanation: **" if present
                explanation = explanation.replaceAll("(?i)^\\*\\*Explanation:?\\s*", "").trim();
                // Remove trailing "**" if present
                if (explanation.endsWith("**")) {
                    explanation = explanation.substring(0, explanation.length() - 2).trim();
                }
                q.setExplanation(explanation);
            }
        }
    }
}
