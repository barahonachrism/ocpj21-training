package com.ocpj21.simulator.repository;

import com.ocpj21.simulator.model.Question;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Question entities.
 */
public interface QuestionRepository {
    List<Question> findAll();

    Optional<Question> findById(String id);

    Question save(Question question);

    List<Question> saveAll(Iterable<Question> questions);

    long count();
}
