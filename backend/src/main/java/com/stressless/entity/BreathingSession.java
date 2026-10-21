package com.stressless.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "breathing_sessions")
public class BreathingSession {

    public enum Technique {
        BOX_BREATHING,
        SLOW_PACED,
        FOUR_SEVEN_EIGHT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Technique technique;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;

    @Column(name = "cycles_completed", nullable = false)
    private Integer cyclesCompleted = 1;

    @Column(name = "completed_at", updatable = false)
    private LocalDateTime completedAt = LocalDateTime.now();

    public BreathingSession() {
    }

    public BreathingSession(User user, Technique technique, Integer durationSeconds, Integer cyclesCompleted) {
        this.user = user;
        this.technique = technique;
        this.durationSeconds = durationSeconds;
        this.cyclesCompleted = cyclesCompleted != null ? cyclesCompleted : 1;
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

    public Technique getTechnique() {
        return technique;
    }

    public void setTechnique(Technique technique) {
        this.technique = technique;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getCyclesCompleted() {
        return cyclesCompleted;
    }

    public void setCyclesCompleted(Integer cyclesCompleted) {
        this.cyclesCompleted = cyclesCompleted;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
