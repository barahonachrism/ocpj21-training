package com.ocpj21.simulator.repository;

import com.ocpj21.simulator.model.Exam;
import java.util.Optional;

/**
 * Repository interface for Exam entities.
 */
public interface ExamRepository {
    Optional<Exam> findById(String id);

    Exam save(Exam exam);
}
