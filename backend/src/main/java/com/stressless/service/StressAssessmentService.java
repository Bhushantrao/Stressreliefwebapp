package com.stressless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stressless.dto.request.StressAssessmentRequest;
import com.stressless.dto.response.StressAssessmentResultDto;
import com.stressless.entity.StressAssessment;
import com.stressless.entity.StressAssessment.StressLevel;
import com.stressless.entity.User;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.repository.StressAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StressAssessmentService {

    @Autowired
    private StressAssessmentRepository assessmentRepository;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public StressAssessmentResultDto submitAssessment(StressAssessmentRequest request) {
        User user = userService.getCurrentAuthenticatedUser();

        // Calculate stress score (0 - 100 scale)
        // Sleep (1=poor -> high stress contribution)
        int sleepScore = (5 - request.getSleepRating()) * 4; // max 16
        // Workload (5=overwhelming -> high stress contribution)
        int workloadScore = (request.getWorkloadRating() - 1) * 5; // max 20
        // Mood (1=low -> high stress contribution)
        int moodScore = (5 - request.getMoodRating()) * 4; // max 16
        // Tension (5=intense -> high stress contribution)
        int tensionScore = (request.getTensionRating() - 1) * 5; // max 20
        // Concentration (1=foggy -> high stress)
        int concentrationScore = (5 - request.getConcentrationRating()) * 3; // max 12
        // Energy (1=exhausted -> high stress)
        int energyScore = (5 - request.getEnergyRating()) * 4; // max 16

        int totalScore = Math.min(100, Math.max(0, sleepScore + workloadScore + moodScore + tensionScore + concentrationScore + energyScore));

        StressLevel stressLevel;
        String summary;
        List<String> recommendations = new ArrayList<>();
        List<String> activities = new ArrayList<>();

        if (totalScore < 35) {
            stressLevel = StressLevel.LOW;
            summary = "Your responses suggest a healthy, balanced state with low current stress.";
            recommendations.add("Maintain your healthy sleep and exercise routines.");
            recommendations.add("Continue daily mindfulness practices to build ongoing mental resilience.");
            recommendations.add("Celebrate your balance and share positive energy with friends or peers.");
            activities.add("5-Minute Morning Clarity Meditation");
            activities.add("Slow-Paced Breathing for daily grounding");
            activities.add("Gratitude Journaling");
        } else if (totalScore <= 65) {
            stressLevel = StressLevel.MODERATE;
            summary = "Your responses indicate moderate stress levels. You may be experiencing deadline pressure, workload friction, or disrupted rest.";
            recommendations.add("Schedule intentional 5-minute pauses between study or work tasks.");
            recommendations.add("Practice Box Breathing to restore nervous system equilibrium.");
            recommendations.add("Unplug from social media and work emails at least 45 minutes before bedtime.");
            recommendations.add("Keep a calm ambient background sound playing during intense focus blocks.");
            activities.add("Box Breathing (4-4-4-4 technique)");
            activities.add("Deep Work Stress Release Guided Meditation");
            activities.add("Gentle Summer Rain & Ocean Soundscapes");
            activities.add("Reflective Journaling to externalize racing thoughts");
        } else {
            stressLevel = StressLevel.HIGH;
            summary = "Your responses reflect elevated stress levels and fatigue. Prioritize self-care, rest, and stress reduction right now.";
            recommendations.add("Give yourself permission to pause. Step away from immediate non-essential tasks.");
            recommendations.add("Engage in 4-7-8 breathing to activate the parasympathetic calm response.");
            recommendations.add("Aim for 7 to 8 hours of restorative sleep in a cool, quiet room.");
            recommendations.add("Talk to a supportive mentor, close friend, or campus/workplace counselor.");
            activities.add("4-7-8 Deep Relaxation Breathing Exercise");
            activities.add("Anxiety & Overwhelm Anchor Meditation");
            activities.add("Tibetan Singing Bowls 432 Hz Sound Therapy");
            activities.add("Venting / Stream of Consciousness Journaling");
        }

        // Store Answers JSON
        Map<String, Object> answersMap = new HashMap<>();
        answersMap.put("sleepRating", request.getSleepRating());
        answersMap.put("workloadRating", request.getWorkloadRating());
        answersMap.put("moodRating", request.getMoodRating());
        answersMap.put("tensionRating", request.getTensionRating());
        answersMap.put("concentrationRating", request.getConcentrationRating());
        answersMap.put("energyRating", request.getEnergyRating());
        answersMap.put("notes", request.getNotes());

        String jsonAnswers = "{}";
        try {
            jsonAnswers = objectMapper.writeValueAsString(answersMap);
        } catch (JsonProcessingException e) {
            // fallback
        }

        StressAssessment assessment = new StressAssessment();
        assessment.setUser(user);
        assessment.setStressScore(totalScore);
        assessment.setStressLevel(stressLevel);
        assessment.setSleepRating(request.getSleepRating());
        assessment.setWorkloadRating(request.getWorkloadRating());
        assessment.setMoodRating(request.getMoodRating());
        assessment.setEnergyRating(request.getEnergyRating());
        assessment.setAnswersJson(jsonAnswers);
        assessment.setRecommendations(String.join(" | ", recommendations));

        StressAssessment saved = assessmentRepository.save(assessment);

        return mapToDto(saved, summary, recommendations, activities);
    }

    public List<StressAssessmentResultDto> getUserAssessmentHistory() {
        User user = userService.getCurrentAuthenticatedUser();
        List<StressAssessment> assessments = assessmentRepository.findByUserIdOrderByAssessedAtDesc(user.getId());

        List<StressAssessmentResultDto> results = new ArrayList<>();
        for (StressAssessment a : assessments) {
            results.add(mapToDto(a, null, null, null));
        }
        return results;
    }

    public Optional<StressAssessmentResultDto> getLatestAssessment() {
        User user = userService.getCurrentAuthenticatedUser();
        return assessmentRepository.findFirstByUserIdOrderByAssessedAtDesc(user.getId())
                .map(a -> mapToDto(a, null, null, null));
    }

    private StressAssessmentResultDto mapToDto(StressAssessment a, String customSummary, List<String> recs, List<String> acts) {
        StressAssessmentResultDto dto = new StressAssessmentResultDto();
        dto.setId(a.getId());
        dto.setStressScore(a.getStressScore());
        dto.setStressLevel(a.getStressLevel().name());
        dto.setAssessedAt(a.getAssessedAt());

        if (customSummary != null) {
            dto.setSummary(customSummary);
        } else {
            dto.setSummary("Reported stress level: " + a.getStressLevel().name());
        }

        if (recs != null && !recs.isEmpty()) {
            dto.setRecommendations(recs);
        } else if (a.getRecommendations() != null) {
            dto.setRecommendations(Arrays.asList(a.getRecommendations().split(" \\| ")));
        } else {
            dto.setRecommendations(Collections.singletonList("Engage in regular deep breathing and hydration."));
        }

        if (acts != null && !acts.isEmpty()) {
            dto.setRecommendedActivities(acts);
        } else {
            if (a.getStressLevel() == StressLevel.HIGH) {
                dto.setRecommendedActivities(Arrays.asList("4-7-8 Breathing", "Anxiety Grounding Meditation", "Rain Sounds"));
            } else if (a.getStressLevel() == StressLevel.MODERATE) {
                dto.setRecommendedActivities(Arrays.asList("Box Breathing", "Deep Work Reset", "Ocean Waves"));
            } else {
                dto.setRecommendedActivities(Arrays.asList("Morning Mindfulness", "Gratitude Journaling", "Nature Sounds"));
            }
        }

        return dto;
    }
}
