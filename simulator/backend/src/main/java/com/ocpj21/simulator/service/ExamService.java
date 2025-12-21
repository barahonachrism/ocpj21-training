package com.ocpj21.simulator.service;

import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.model.ExamQuestion;
import com.ocpj21.simulator.model.Question;
import com.ocpj21.simulator.repository.ExamRepository;
import com.ocpj21.simulator.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing exams.
 * Handles starting new exams, submitting answers, and retrieving results.
 */
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    /**
     * Constructs an ExamService with the necessary repositories.
     *
     * @param examRepository     the repository for managing Exam entities
     * @param questionRepository the repository for managing Question entities
     */
    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Starts a new exam by selecting 50 random questions.
     *
     * @return the newly created Exam entity
     * @throws RuntimeException if no questions are available in the repository
     */
    /**
     * Starts a new exam by selecting 50 random questions.
     *
     * @return the newly created Exam entity
     * @throws RuntimeException if no questions are available in the repository
     */
    public Exam startExam() {
        List<Question> allQuestions = questionRepository.findAll();
        if (allQuestions.isEmpty()) {
            throw new RuntimeException("No questions available. Please check if questions are loaded.");
        }

        // Select 50 random questions
        Collections.shuffle(allQuestions);
        List<Question> selectedQuestions = allQuestions.stream()
                .limit(50)
                .collect(Collectors.toList());

        Exam exam = new Exam();
        exam.setStartTime(LocalDateTime.now());

        List<ExamQuestion> examQuestions = new ArrayList<>();
        for (Question q : selectedQuestions) {
            ExamQuestion eq = new ExamQuestion();
            eq.setId(UUID.randomUUID().toString());
            eq.setQuestion(q);
            examQuestions.add(eq);
        }

        exam.setQuestions(examQuestions);
        return examRepository.save(exam);
    }

    /**
     * Submits an exam by recording the user's answers and calculating the score.
     *
     * @param examId  the ID of the exam being submitted
     * @param answers a map of question IDs to the list of selected option labels
     * @return the updated Exam entity with results
     * @throws RuntimeException if the exam is not found or has already been
     *                          submitted
     */
    public Exam submitExam(String examId, Map<String, List<String>> answers) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (exam.getEndTime() != null) {
            throw new RuntimeException("Exam already submitted");
        }

        exam.setEndTime(LocalDateTime.now());
        int correctCount = 0;

        for (ExamQuestion eq : exam.getQuestions()) {
            List<String> userAnswers = answers.getOrDefault(eq.getQuestion().getId(), Collections.emptyList());
            eq.setSelectedOptions(userAnswers);

            List<String> correctAnswers = eq.getQuestion().getCorrectAnswers();

            // Check if answers match (ignoring order)
            boolean isCorrect = new HashSet<>(userAnswers).equals(new HashSet<>(correctAnswers));
            eq.setIsCorrect(isCorrect);

            if (isCorrect) {
                correctCount++;
            }
        }

        exam.setScore(correctCount);
        // Passing score is 68% of 50 = 34 questions
        exam.setPassed(correctCount >= 34);

        return examRepository.save(exam);
    }

    /**
     * Retrieves an exam by its ID.
     *
     * @param id the ID of the exam to retrieve
     * @return the Exam entity
     * @throws RuntimeException if the exam is not found
     */
    public Exam getExam(String id) {
        System.out.println("ExamService: getExam called for id: " + id);
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        System.out.println("ExamService: Exam found. Returning exam.");
        return exam;
    }

    /**
     * Retrieves the results of an exam and maps them to a DTO.
     *
     * @param id the ID of the exam
     * @return an ExamResultDTO containing the exam details and results
     */
    public com.ocpj21.simulator.dto.ExamResultDTO getExamResult(String id) {
        Exam exam = getExam(id);

        com.ocpj21.simulator.dto.ExamResultDTO dto = new com.ocpj21.simulator.dto.ExamResultDTO();
        dto.setId(exam.getId());
        dto.setStartTime(exam.getStartTime());
        dto.setEndTime(exam.getEndTime());
        dto.setScore(exam.getScore());
        dto.setPassed(exam.getPassed());

        List<com.ocpj21.simulator.dto.ExamQuestionDTO> questionDTOs = exam.getQuestions().stream().map(eq -> {
            com.ocpj21.simulator.dto.ExamQuestionDTO eqDto = new com.ocpj21.simulator.dto.ExamQuestionDTO();
            eqDto.setId(eq.getId());
            eqDto.setSelectedOptions(new ArrayList<>(eq.getSelectedOptions()));
            eqDto.setIsCorrect(eq.getIsCorrect());

            com.ocpj21.simulator.dto.QuestionDTO qDto = new com.ocpj21.simulator.dto.QuestionDTO();
            Question q = eq.getQuestion();
            qDto.setId(q.getId());
            qDto.setChapter(q.getChapter());
            qDto.setQuestionNumber(q.getQuestionNumber());
            qDto.setText(q.getText());
            qDto.setCodeSnippet(q.getCodeSnippet());
            qDto.setCorrectAnswers(new ArrayList<>(q.getCorrectAnswers()));
            qDto.setExplanation(q.getExplanation());

            List<com.ocpj21.simulator.dto.OptionDTO> optionDTOs = q.getOptions().stream().map(o -> {
                com.ocpj21.simulator.dto.OptionDTO oDto = new com.ocpj21.simulator.dto.OptionDTO();
                oDto.setId(o.getId());
                oDto.setLabel(o.getLabel());
                oDto.setText(o.getText());
                return oDto;
            }).collect(Collectors.toList());
            qDto.setOptions(optionDTOs);

            eqDto.setQuestion(qDto);
            return eqDto;
        }).collect(Collectors.toList());

        System.out.println("ExamService: getExamResult constructed DTO. Questions size: " + questionDTOs.size());
        dto.setQuestions(questionDTOs);
        return dto;
    }
}
