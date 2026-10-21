package com.stressless.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class StressAssessmentResultDto {

    private Long id;
    private Integer stressScore; // 0 - 100 scale
    private String stressLevel; // LOW, MODERATE, HIGH
    private String summary;
    private String medicalDisclaimer = "Notice: This stress assessment is an educational self-reflection tool and is NOT a medical or psychological diagnosis. If you are experiencing persistent distress, please consult a qualified healthcare or mental health professional.";
    private List<String> recommendations;
    private List<String> recommendedActivities;
    private LocalDateTime assessedAt;

    public StressAssessmentResultDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStressScore() {
        return stressScore;
    }

    public void setStressScore(Integer stressScore) {
        this.stressScore = stressScore;
    }

    public String getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(String stressLevel) {
        this.stressLevel = stressLevel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMedicalDisclaimer() {
        return medicalDisclaimer;
    }

    public void setMedicalDisclaimer(String medicalDisclaimer) {
        this.medicalDisclaimer = medicalDisclaimer;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getRecommendedActivities() {
        return recommendedActivities;
    }

    public void setRecommendedActivities(List<String> recommendedActivities) {
        this.recommendedActivities = recommendedActivities;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(LocalDateTime assessedAt) {
        this.assessedAt = assessedAt;
    }
}
