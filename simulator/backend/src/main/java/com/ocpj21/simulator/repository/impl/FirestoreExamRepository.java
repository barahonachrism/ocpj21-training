package com.ocpj21.simulator.repository.impl;

import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import com.ocpj21.simulator.model.Exam;
import com.ocpj21.simulator.repository.ExamRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Firestore implementation of ExamRepository for the prod profile.
 */
@Repository
@Profile("prod")
public class FirestoreExamRepository implements ExamRepository {

    private final FirestoreTemplate firestoreTemplate;

    public FirestoreExamRepository(FirestoreTemplate firestoreTemplate) {
        this.firestoreTemplate = firestoreTemplate;
    }

    @Override
    public Optional<Exam> findById(String id) {
        return Optional.ofNullable(firestoreTemplate.findById(Mono.just(id), Exam.class).block());
    }

    @Override
    public Exam save(Exam exam) {
        return firestoreTemplate.save(exam).block();
    }
}
