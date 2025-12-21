package com.ocpj21.simulator.repository.impl;

import com.ocpj21.simulator.model.Question;
import com.ocpj21.simulator.repository.QuestionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of QuestionRepository for the dev profile.
 */
@Repository
@Profile("dev")
public class InMemoryQuestionRepository implements QuestionRepository {
    private final Map<String, Question> questions = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(questions.values());
    }

    @Override
    public Optional<Question> findById(String id) {
        return Optional.ofNullable(questions.get(id));
    }

    @Override
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId(String.valueOf(idGenerator.getAndIncrement()));
        }
        questions.put(question.getId(), question);
        return question;
    }

    @Override
    public List<Question> saveAll(Iterable<Question> questions) {
        List<Question> saved = new ArrayList<>();
        for (Question q : questions) {
            saved.add(save(q));
        }
        return saved;
    }

    @Override
    public long count() {
        return questions.size();
    }
}
