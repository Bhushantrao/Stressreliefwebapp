package com.stressless.repository;

import com.stressless.entity.BreathingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BreathingSessionRepository extends JpaRepository<BreathingSession, Long> {
    List<BreathingSession> findByUserIdOrderByCompletedAtDesc(Long userId);

    @Query("SELECT COUNT(b) FROM BreathingSession b WHERE b.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(b.durationSeconds), 0) FROM BreathingSession b WHERE b.user.id = :userId")
    long sumDurationSecondsByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(b.durationSeconds), 0) FROM BreathingSession b WHERE b.user.id = :userId AND b.completedAt >= :startDate")
    long sumRecentDurationSeconds(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT DISTINCT DATE(b.completedAt) FROM BreathingSession b WHERE b.user.id = :userId ORDER BY DATE(b.completedAt) DESC")
    List<Object> findDistinctActiveDatesByUserId(@Param("userId") Long userId);
}
