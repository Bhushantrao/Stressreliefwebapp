package com.stressless.repository;

import com.stressless.entity.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {
    List<MoodEntry> findByUserIdOrderByLoggedAtDesc(Long userId);
    Optional<MoodEntry> findFirstByUserIdOrderByLoggedAtDesc(Long userId);

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId AND m.loggedAt >= :startDate ORDER BY m.loggedAt ASC")
    List<MoodEntry> findRecentMoods(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(m) FROM MoodEntry m WHERE m.user.id = :userId AND m.loggedAt >= :startOfDay AND m.loggedAt <= :endOfDay")
    long countTodayCheckins(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    long countByUserId(Long userId);
}
