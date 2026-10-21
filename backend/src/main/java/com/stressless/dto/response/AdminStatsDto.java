package com.stressless.dto.response;

public class AdminStatsDto {

    private long totalUsers;
    private long totalMeditationSessions;
    private long totalQuotes;
    private long totalRelaxationResources;
    private long totalBreathingSessionsCompleted;
    private long totalMeditationCompletions;
    private long totalMoodLogs;
    private long totalJournalEntries;

    public AdminStatsDto() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalMeditationSessions() {
        return totalMeditationSessions;
    }

    public void setTotalMeditationSessions(long totalMeditationSessions) {
        this.totalMeditationSessions = totalMeditationSessions;
    }

    public long getTotalQuotes() {
        return totalQuotes;
    }

    public void setTotalQuotes(long totalQuotes) {
        this.totalQuotes = totalQuotes;
    }

    public long getTotalRelaxationResources() {
        return totalRelaxationResources;
    }

    public void setTotalRelaxationResources(long totalRelaxationResources) {
        this.totalRelaxationResources = totalRelaxationResources;
    }

    public long getTotalBreathingSessionsCompleted() {
        return totalBreathingSessionsCompleted;
    }

    public void setTotalBreathingSessionsCompleted(long totalBreathingSessionsCompleted) {
        this.totalBreathingSessionsCompleted = totalBreathingSessionsCompleted;
    }

    public long getTotalMeditationCompletions() {
        return totalMeditationCompletions;
    }

    public void setTotalMeditationCompletions(long totalMeditationCompletions) {
        this.totalMeditationCompletions = totalMeditationCompletions;
    }

    public long getTotalMoodLogs() {
        return totalMoodLogs;
    }

    public void setTotalMoodLogs(long totalMoodLogs) {
        this.totalMoodLogs = totalMoodLogs;
    }

    public long getTotalJournalEntries() {
        return totalJournalEntries;
    }

    public void setTotalJournalEntries(long totalJournalEntries) {
        this.totalJournalEntries = totalJournalEntries;
    }
}
