package com.stressless.service;

import com.stressless.dto.request.MoodEntryRequest;
import com.stressless.dto.response.MoodTrendDto;
import com.stressless.entity.MoodEntry;
import com.stressless.entity.User;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.exception.UnauthorizedAccessException;
import com.stressless.repository.MoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MoodService {

    @Autowired
    private MoodEntryRepository moodRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WellnessGoalService wellnessGoalService;

    @Transactional
    public MoodEntry logMood(MoodEntryRequest request) {
        User user = userService.getCurrentAuthenticatedUser();

        MoodEntry entry = new MoodEntry(
                user,
                request.getMood(),
                request.getEnergyLevel(),
                request.getNotes(),
                request.getTags()
        );

        MoodEntry saved = moodRepository.save(entry);

        // Update any mood check-in goals
        wellnessGoalService.incrementGoalProgress(user.getId(), com.stressless.entity.WellnessGoal.GoalTargetType.MOOD_CHECKINS, 1);

        return saved;
    }

    public List<MoodEntry> getMoodHistory() {
        User user = userService.getCurrentAuthenticatedUser();
        return moodRepository.findByUserIdOrderByLoggedAtDesc(user.getId());
    }

    public MoodTrendDto getMoodTrends(int days) {
        User user = userService.getCurrentAuthenticatedUser();
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<MoodEntry> entries = moodRepository.findRecentMoods(user.getId(), startDate);

        MoodTrendDto dto = new MoodTrendDto();

        Map<String, Long> distribution = new LinkedHashMap<>();
        distribution.put("HAPPY", 0L);
        distribution.put("CALM", 0L);
        distribution.put("NEUTRAL", 0L);
        distribution.put("ANXIOUS", 0L);
        distribution.put("SAD", 0L);

        List<Map<String, Object>> timeline = new ArrayList<>();
        double totalEnergy = 0;
        int count = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        for (MoodEntry e : entries) {
            String moodName = e.getMood().name();
            distribution.put(moodName, distribution.getOrDefault(moodName, 0L) + 1);

            totalEnergy += (e.getEnergyLevel() != null ? e.getEnergyLevel() : 3);
            count++;

            Map<String, Object> point = new HashMap<>();
            point.put("id", e.getId());
            point.put("date", e.getLoggedAt().format(formatter));
            point.put("mood", moodName);
            point.put("energy", e.getEnergyLevel());
            point.put("notes", e.getNotes());
            timeline.add(point);
        }

        dto.setMoodDistribution(distribution);
        dto.setDailyTimeline(timeline);
        dto.setAverageEnergy(count > 0 ? Math.round((totalEnergy / count) * 10.0) / 10.0 : 3.0);

        // Find predominant mood
        String predominant = "NEUTRAL";
        long maxCount = -1;
        for (Map.Entry<String, Long> entry : distribution.entrySet()) {
            if (entry.getValue() > maxCount && entry.getValue() > 0) {
                maxCount = entry.getValue();
                predominant = entry.getKey();
            }
        }
        dto.setPredominantMood(predominant);

        // Generate tailored insight
        if (count == 0) {
            dto.setInsight("No mood logs recorded in this period yet. Check in daily to track your emotional trends!");
        } else if ("HAPPY".equals(predominant) || "CALM".equals(predominant)) {
            dto.setInsight("You have been experiencing positive and grounded states frequently! Keep nurturing your current routines.");
        } else if ("ANXIOUS".equals(predominant)) {
            dto.setInsight("You've experienced elevated anxious moods recently. Try integrating daily Box Breathing and limiting evening screen exposure.");
        } else if ("SAD".equals(predominant)) {
            dto.setInsight("You've logged low or sad moods lately. Be gentle with yourself, consider journaling your thoughts, and connect with people who support you.");
        } else {
            dto.setInsight("Your mood trends are fairly balanced. Notice what activities correspond to higher energy days.");
        }

        return dto;
    }

    @Transactional
    public void deleteMoodEntry(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        MoodEntry entry = moodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mood entry not found with id: " + id));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You can only delete your own mood entries");
        }

        moodRepository.delete(entry);
    }
}
