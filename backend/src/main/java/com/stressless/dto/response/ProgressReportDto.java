package com.stressless.dto.response;

import com.stressless.entity.WellnessGoal;

import java.util.List;
import java.util.Map;

public class ProgressReportDto {

    private int streakDays;
    private long totalBreathingSessions;
    private long totalBreathingMinutes;
    private long totalMeditationSessions;
    private long totalMeditationMinutes;
    private long totalJournalEntries;
    private long totalMoodCheckins;
    private List<Map<String, Object>> weeklyActivityChart; // dayName, breathingMins, meditationMins, checkins
    private List<WellnessGoal> goals;
    private double overallGoalCompletionPercentage;

    public ProgressReportDto() {
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public long getTotalBreathingSessions() {
        return totalBreathingSessions;
    }

    public void setTotalBreathingSessions(long totalBreathingSessions) {
        this.totalBreathingSessions = totalBreathingSessions;
    }

    public long getTotalBreathingMinutes() {
        return totalBreathingMinutes;
    }

    public void setTotalBreathingMinutes(long totalBreathingMinutes) {
        this.totalBreathingMinutes = totalBreathingMinutes;
    }

    public long getTotalMeditationSessions() {
        return totalMeditationSessions;
    }

    public void setTotalMeditationSessions(long totalMeditationSessions) {
        this.totalMeditationSessions = totalMeditationSessions;
    }

    public long getTotalMeditationMinutes() {
        return totalMeditationMinutes;
    }

    public void setTotalMeditationMinutes(long totalMeditationMinutes) {
        this.totalMeditationMinutes = totalMeditationMinutes;
    }

    public long getTotalJournalEntries() {
        return totalJournalEntries;
    }

    public void setTotalJournalEntries(long totalJournalEntries) {
        this.totalJournalEntries = totalJournalEntries;
    }

    public long getTotalMoodCheckins() {
        return totalMoodCheckins;
    }

    public void setTotalMoodCheckins(long totalMoodCheckins) {
        this.totalMoodCheckins = totalMoodCheckins;
    }

    public List<Map<String, Object>> getWeeklyActivityChart() {
        return weeklyActivityChart;
    }

    public void setWeeklyActivityChart(List<Map<String, Object>> weeklyActivityChart) {
        this.weeklyActivityChart = weeklyActivityChart;
    }

    public List<WellnessGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<WellnessGoal> goals) {
        this.goals = goals;
    }

    public double getOverallGoalCompletionPercentage() {
        return overallGoalCompletionPercentage;
    }

    public void setOverallGoalCompletionPercentage(double overallGoalCompletionPercentage) {
        this.overallGoalCompletionPercentage = overallGoalCompletionPercentage;
    }
}
