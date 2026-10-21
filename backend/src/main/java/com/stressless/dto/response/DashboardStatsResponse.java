package com.stressless.dto.response;

import com.stressless.entity.MotivationalQuote;

import java.util.List;
import java.util.Map;

public class DashboardStatsResponse {

    private String welcomeMessage;
    private MotivationalQuote dailyQuote;
    private String todayMood;
    private Integer todayEnergy;
    private Integer currentStreakDays;
    private Integer totalBreathingMinutes;
    private Integer totalMeditationMinutes;
    private Long journalCount;
    private Long assessmentCount;
    private String latestStressLevel;
    private Integer latestStressScore;
    private List<Map<String, Object>> recentActivities;
    private List<Map<String, Object>> activeGoals;

    public DashboardStatsResponse() {
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public MotivationalQuote getDailyQuote() {
        return dailyQuote;
    }

    public void setDailyQuote(MotivationalQuote dailyQuote) {
        this.dailyQuote = dailyQuote;
    }

    public String getTodayMood() {
        return todayMood;
    }

    public void setTodayMood(String todayMood) {
        this.todayMood = todayMood;
    }

    public Integer getTodayEnergy() {
        return todayEnergy;
    }

    public void setTodayEnergy(Integer todayEnergy) {
        this.todayEnergy = todayEnergy;
    }

    public Integer getCurrentStreakDays() {
        return currentStreakDays;
    }

    public void setCurrentStreakDays(Integer currentStreakDays) {
        this.currentStreakDays = currentStreakDays;
    }

    public Integer getTotalBreathingMinutes() {
        return totalBreathingMinutes;
    }

    public void setTotalBreathingMinutes(Integer totalBreathingMinutes) {
        this.totalBreathingMinutes = totalBreathingMinutes;
    }

    public Integer getTotalMeditationMinutes() {
        return totalMeditationMinutes;
    }

    public void setTotalMeditationMinutes(Integer totalMeditationMinutes) {
        this.totalMeditationMinutes = totalMeditationMinutes;
    }

    public Long getJournalCount() {
        return journalCount;
    }

    public void setJournalCount(Long journalCount) {
        this.journalCount = journalCount;
    }

    public Long getAssessmentCount() {
        return assessmentCount;
    }

    public void setAssessmentCount(Long assessmentCount) {
        this.assessmentCount = assessmentCount;
    }

    public String getLatestStressLevel() {
        return latestStressLevel;
    }

    public void setLatestStressLevel(String latestStressLevel) {
        this.latestStressLevel = latestStressLevel;
    }

    public Integer getLatestStressScore() {
        return latestStressScore;
    }

    public void setLatestStressScore(Integer latestStressScore) {
        this.latestStressScore = latestStressScore;
    }

    public List<Map<String, Object>> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<Map<String, Object>> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public List<Map<String, Object>> getActiveGoals() {
        return activeGoals;
    }

    public void setActiveGoals(List<Map<String, Object>> activeGoals) {
        this.activeGoals = activeGoals;
    }
}
