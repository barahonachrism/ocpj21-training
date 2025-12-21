package com.ocpj21.simulator.repository.impl;

import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.repository.ExamRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of ExamRepository for the dev profile.
 */
@Repository
@Profile("dev")
public class InMemoryExamRepository implements ExamRepository {
    private final Map<String, Exam> exams = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<Exam> findById(String id) {
        return Optional.ofNullable(exams.get(id));
    }

    @Override
    public Exam save(Exam exam) {
        if (exam.getId() == null) {
            exam.setId(String.valueOf(idGenerator.getAndIncrement()));
        }
        exams.put(exam.getId(), exam);
        return exam;
    }
}
