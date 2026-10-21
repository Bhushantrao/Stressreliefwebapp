package com.stressless.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_meditation_completions")
public class UserMeditationCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meditation_session_id", nullable = false)
    private MeditationSession meditationSession;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds = 0;

    @Column(name = "completed_at", updatable = false)
    private LocalDateTime completedAt = LocalDateTime.now();

    public UserMeditationCompletion() {
    }

    public UserMeditationCompletion(User user, MeditationSession meditationSession, Integer durationSeconds) {
        this.user = user;
        this.meditationSession = meditationSession;
        this.durationSeconds = durationSeconds != null ? durationSeconds : 0;
        this.completedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MeditationSession getMeditationSession() {
        return meditationSession;
    }

    public void setMeditationSession(MeditationSession meditationSession) {
        this.meditationSession = meditationSession;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
