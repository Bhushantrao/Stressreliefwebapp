package com.stressless.dto.response;

import java.util.List;
import java.util.Map;

public class MoodTrendDto {

    private Map<String, Long> moodDistribution; // e.g. HAPPY: 5, CALM: 8...
    private Double averageEnergy;
    private List<Map<String, Object>> dailyTimeline; // date, mood, energy, notes
    private String predominantMood;
    private String insight;

    public MoodTrendDto() {
    }

    public Map<String, Long> getMoodDistribution() {
        return moodDistribution;
    }

    public void setMoodDistribution(Map<String, Long> moodDistribution) {
        this.moodDistribution = moodDistribution;
    }

    public Double getAverageEnergy() {
        return averageEnergy;
    }

    public void setAverageEnergy(Double averageEnergy) {
        this.averageEnergy = averageEnergy;
    }

    public List<Map<String, Object>> getDailyTimeline() {
        return dailyTimeline;
    }

    public void setDailyTimeline(List<Map<String, Object>> dailyTimeline) {
        this.dailyTimeline = dailyTimeline;
    }

    public String getPredominantMood() {
        return predominantMood;
    }

    public void setPredominantMood(String predominantMood) {
        this.predominantMood = predominantMood;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }
}
