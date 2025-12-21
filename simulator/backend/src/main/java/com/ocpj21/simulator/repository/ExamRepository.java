package com.ocpj21.simulator.repository;

import com.ocpj21.simulator.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Exam entities.
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
}
