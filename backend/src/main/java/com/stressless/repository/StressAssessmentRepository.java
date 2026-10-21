package com.stressless.repository;

import com.stressless.entity.StressAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StressAssessmentRepository extends JpaRepository<StressAssessment, Long> {
    List<StressAssessment> findByUserIdOrderByAssessedAtDesc(Long userId);
    Optional<StressAssessment> findFirstByUserIdOrderByAssessedAtDesc(Long userId);
    long countByUserId(Long userId);
}
