package com.stressless.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stress_assessments")
public class StressAssessment {

    public enum StressLevel {
        LOW,
        MODERATE,
        HIGH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "stress_score", nullable = false)
    private Integer stressScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "stress_level", nullable = false, length = 20)
    private StressLevel stressLevel;

    @Column(name = "sleep_rating")
    private Integer sleepRating;

    @Column(name = "workload_rating")
    private Integer workloadRating;

    @Column(name = "mood_rating")
    private Integer moodRating;

    @Column(name = "energy_rating")
    private Integer energyRating;

    @Column(name = "answers_json", columnDefinition = "TEXT")
    private String answersJson;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "assessed_at", updatable = false)
    private LocalDateTime assessedAt = LocalDateTime.now();

    public StressAssessment() {
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

    public Integer getStressScore() {
        return stressScore;
    }

    public void setStressScore(Integer stressScore) {
        this.stressScore = stressScore;
    }

    public StressLevel getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(StressLevel stressLevel) {
        this.stressLevel = stressLevel;
    }

    public Integer getSleepRating() {
        return sleepRating;
    }

    public void setSleepRating(Integer sleepRating) {
        this.sleepRating = sleepRating;
    }

    public Integer getWorkloadRating() {
        return workloadRating;
    }

    public void setWorkloadRating(Integer workloadRating) {
        this.workloadRating = workloadRating;
    }

    public Integer getMoodRating() {
        return moodRating;
    }

    public void setMoodRating(Integer moodRating) {
        this.moodRating = moodRating;
    }

    public Integer getEnergyRating() {
        return energyRating;
    }

    public void setEnergyRating(Integer energyRating) {
        this.energyRating = energyRating;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(LocalDateTime assessedAt) {
        this.assessedAt = assessedAt;
    }
}
