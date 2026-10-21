package com.stressless.repository;

import com.stressless.entity.MeditationSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeditationSessionRepository extends JpaRepository<MeditationSession, Long> {
    List<MeditationSession> findByCategoryIgnoreCase(String category);
    List<MeditationSession> findAllByOrderByCreatedAtDesc();
}
