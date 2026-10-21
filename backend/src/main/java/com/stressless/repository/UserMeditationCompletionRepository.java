package com.stressless.repository;

import com.stressless.entity.UserMeditationCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserMeditationCompletionRepository extends JpaRepository<UserMeditationCompletion, Long> {
    List<UserMeditationCompletion> findByUserIdOrderByCompletedAtDesc(Long userId);

    @Query("SELECT COUNT(c) FROM UserMeditationCompletion c WHERE c.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(c.durationSeconds), 0) FROM UserMeditationCompletion c WHERE c.user.id = :userId")
    long sumDurationSecondsByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(c.durationSeconds), 0) FROM UserMeditationCompletion c WHERE c.user.id = :userId AND c.completedAt >= :startDate")
    long sumRecentDurationSeconds(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
}
