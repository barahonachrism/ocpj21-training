package com.ocpj21.simulator.repository;

import com.ocpj21.simulator.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Question entities.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
