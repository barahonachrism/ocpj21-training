package com.ocpj21.simulator.repository.impl;

import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import com.ocpj21.simulator.model.Question;
import com.ocpj21.simulator.repository.QuestionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Firestore implementation of QuestionRepository for the prod profile.
 */
@Repository
@Profile("prod")
public class FirestoreQuestionRepository implements QuestionRepository {

    private final FirestoreTemplate firestoreTemplate;

    public FirestoreQuestionRepository(FirestoreTemplate firestoreTemplate) {
        this.firestoreTemplate = firestoreTemplate;
    }

    @Override
    public List<Question> findAll() {
        return firestoreTemplate.findAll(Question.class).collectList().block();
    }

    @Override
    public Optional<Question> findById(String id) {
        return Optional.ofNullable(firestoreTemplate.findById(Mono.just(id), Question.class).block());
    }

    @Override
    public Question save(Question question) {
        return firestoreTemplate.save(question).block();
    }

    @Override
    public List<Question> saveAll(Iterable<Question> questions) {
        return firestoreTemplate.saveAll(Flux.fromIterable(questions)).collectList().block();
    }

    @Override
    public long count() {
        return firestoreTemplate.count(Question.class).block();
    }
}
