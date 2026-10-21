package com.stressless.repository;

import com.stressless.entity.WellnessGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WellnessGoalRepository extends JpaRepository<WellnessGoal, Long> {
    List<WellnessGoal> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<WellnessGoal> findByUserIdAndCompletedFalse(Long userId);
    Optional<WellnessGoal> findByIdAndUserId(Long id, Long userId);
}
