package com.stressless.service;

import com.stressless.dto.response.DashboardStatsResponse;
import com.stressless.dto.response.ProgressReportDto;
import com.stressless.entity.*;
import com.stressless.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private UserService userService;

    @Autowired
    private MotivationalQuoteService quoteService;

    @Autowired
    private BreathingSessionRepository breathingRepository;

    @Autowired
    private UserMeditationCompletionRepository meditationCompletionRepository;

    @Autowired
    private MoodEntryRepository moodRepository;

    @Autowired
    private JournalEntryRepository journalRepository;

    @Autowired
    private StressAssessmentRepository assessmentRepository;

    @Autowired
    private WellnessGoalRepository goalRepository;

    @Autowired
    private BreathingService breathingService;

    public DashboardStatsResponse getDashboardStats() {
        User user = userService.getCurrentAuthenticatedUser();
        DashboardStatsResponse response = new DashboardStatsResponse();

        // Personalized welcome based on current time
        int hour = LocalDateTime.now().getHour();
        String greeting = (hour < 12) ? "Good morning" : (hour < 17) ? "Good afternoon" : "Good evening";
        response.setWelcomeMessage(greeting + ", " + user.getName() + "! Breathe in peace, exhale tension.");

        // Daily Motivational Quote
        response.setDailyQuote(quoteService.getDailyQuote());

        // Latest Mood Today
        Optional<MoodEntry> latestMood = moodRepository.findFirstByUserIdOrderByLoggedAtDesc(user.getId());
        if (latestMood.isPresent()) {
            response.setTodayMood(latestMood.get().getMood().name());
            response.setTodayEnergy(latestMood.get().getEnergyLevel());
        } else {
            response.setTodayMood("NOT_LOGGED");
            response.setTodayEnergy(null);
        }

        // Streak Days
        int streak = breathingService.calculateUserStreakDays(user.getId());
        response.setCurrentStreakDays(streak);

        // Breathing & Meditation Totals
        long breathingSeconds = breathingRepository.sumDurationSecondsByUserId(user.getId());
        long meditationSeconds = meditationCompletionRepository.sumDurationSecondsByUserId(user.getId());
        response.setTotalBreathingMinutes((int) Math.round(breathingSeconds / 60.0));
        response.setTotalMeditationMinutes((int) Math.round(meditationSeconds / 60.0));

        // Journal & Assessment count
        response.setJournalCount(journalRepository.countByUserId(user.getId()));
        response.setAssessmentCount(assessmentRepository.countByUserId(user.getId()));

        // Latest stress assessment
        Optional<StressAssessment> latestAssessment = assessmentRepository.findFirstByUserIdOrderByAssessedAtDesc(user.getId());
        if (latestAssessment.isPresent()) {
            response.setLatestStressLevel(latestAssessment.get().getStressLevel().name());
            response.setLatestStressScore(latestAssessment.get().getStressScore());
        } else {
            response.setLatestStressLevel("NOT_ASSESSED");
            response.setLatestStressScore(null);
        }

        // Recent Activities list
        List<Map<String, Object>> activities = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, hh:mm a");

        // Latest breathing
        List<BreathingSession> recentBreathing = breathingRepository.findByUserIdOrderByCompletedAtDesc(user.getId());
        for (int i = 0; i < Math.min(3, recentBreathing.size()); i++) {
            BreathingSession b = recentBreathing.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("type", "BREATHING");
            item.put("icon", "bi-wind");
            item.put("title", b.getTechnique().name().replace("_", " "));
            item.put("detail", (b.getDurationSeconds() / 60) + " mins completed (" + b.getCyclesCompleted() + " cycles)");
            item.put("timestamp", b.getCompletedAt().format(dtf));
            activities.add(item);
        }

        // Latest meditation
        List<UserMeditationCompletion> recentMeditation = meditationCompletionRepository.findByUserIdOrderByCompletedAtDesc(user.getId());
        for (int i = 0; i < Math.min(3, recentMeditation.size()); i++) {
            UserMeditationCompletion m = recentMeditation.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("type", "MEDITATION");
            item.put("icon", "bi-flower1");
            item.put("title", m.getMeditationSession().getTitle());
            item.put("detail", (m.getDurationSeconds() / 60) + " mins completed");
            item.put("timestamp", m.getCompletedAt().format(dtf));
            activities.add(item);
        }

        // Latest journal
        List<JournalEntry> recentJournals = journalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        for (int i = 0; i < Math.min(2, recentJournals.size()); i++) {
            JournalEntry j = recentJournals.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("type", "JOURNAL");
            item.put("icon", "bi-journal-bookmark");
            item.put("title", j.getTitle());
            item.put("detail", "Mood tag: " + (j.getMoodTag() != null ? j.getMoodTag() : "None"));
            item.put("timestamp", j.getCreatedAt().format(dtf));
            activities.add(item);
        }

        response.setRecentActivities(activities);

        // Active Goals
        List<WellnessGoal> goals = goalRepository.findByUserIdAndCompletedFalse(user.getId());
        List<Map<String, Object>> goalMaps = new ArrayList<>();
        for (WellnessGoal g : goals) {
            Map<String, Object> gMap = new HashMap<>();
            gMap.put("id", g.getId());
            gMap.put("title", g.getTitle());
            gMap.put("current", g.getCurrentValue());
            gMap.put("target", g.getTargetValue());
            gMap.put("percentage", Math.min(100, (int) Math.round((g.getCurrentValue() * 100.0) / g.getTargetValue())));
            gMap.put("targetDate", g.getTargetDate().toString());
            goalMaps.add(gMap);
        }
        response.setActiveGoals(goalMaps);

        return response;
    }

    public ProgressReportDto getProgressReport() {
        User user = userService.getCurrentAuthenticatedUser();
        ProgressReportDto dto = new ProgressReportDto();

        dto.setStreakDays(breathingService.calculateUserStreakDays(user.getId()));
        dto.setTotalBreathingSessions(breathingRepository.countByUserId(user.getId()));
        dto.setTotalBreathingMinutes(Math.round(breathingRepository.sumDurationSecondsByUserId(user.getId()) / 60.0));
        dto.setTotalMeditationSessions(meditationCompletionRepository.countByUserId(user.getId()));
        dto.setTotalMeditationMinutes(Math.round(meditationCompletionRepository.sumDurationSecondsByUserId(user.getId()) / 60.0));
        dto.setTotalJournalEntries(journalRepository.countByUserId(user.getId()));
        dto.setTotalMoodCheckins(moodRepository.countByUserId(user.getId()));

        List<WellnessGoal> userGoals = goalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        dto.setGoals(userGoals);

        long completedCount = userGoals.stream().filter(WellnessGoal::isCompleted).count();
        double completionRate = userGoals.isEmpty() ? 0.0 : Math.round((completedCount * 100.0 / userGoals.size()) * 10.0) / 10.0;
        dto.setOverallGoalCompletionPercentage(completionRate);

        // Build 7-day activity chart
        List<Map<String, Object>> weeklyChart = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);

            long breathSecs = breathingRepository.sumRecentDurationSeconds(user.getId(), start);
            long medSecs = meditationCompletionRepository.sumRecentDurationSeconds(user.getId(), start);
            long checkins = moodRepository.countTodayCheckins(user.getId(), start, end);

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", date.format(dayFormatter));
            dayData.put("date", date.toString());
            dayData.put("breathingMins", Math.round(breathSecs / 60.0));
            dayData.put("meditationMins", Math.round(medSecs / 60.0));
            dayData.put("checkins", checkins);
            weeklyChart.add(dayData);
        }

        dto.setWeeklyActivityChart(weeklyChart);

        return dto;
    }
}
