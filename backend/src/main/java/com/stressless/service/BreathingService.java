package com.stressless.service;

import com.stressless.dto.request.BreathingSessionRequest;
import com.stressless.entity.BreathingSession;
import com.stressless.entity.User;
import com.stressless.entity.WellnessGoal.GoalTargetType;
import com.stressless.repository.BreathingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class BreathingService {

    @Autowired
    private BreathingSessionRepository breathingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WellnessGoalService wellnessGoalService;

    @Transactional
    public BreathingSession logSession(BreathingSessionRequest request) {
        User user = userService.getCurrentAuthenticatedUser();

        BreathingSession session = new BreathingSession(
                user,
                request.getTechnique(),
                request.getDurationSeconds(),
                request.getCyclesCompleted()
        );

        BreathingSession saved = breathingRepository.save(session);

        // Update wellness goals for breathing minutes
        int minutes = (int) Math.ceil(request.getDurationSeconds() / 60.0);
        wellnessGoalService.incrementGoalProgress(user.getId(), GoalTargetType.BREATHING_MINUTES, minutes);

        return saved;
    }

    public List<BreathingSession> getUserBreathingHistory() {
        User user = userService.getCurrentAuthenticatedUser();
        return breathingRepository.findByUserIdOrderByCompletedAtDesc(user.getId());
    }

    public int calculateUserStreakDays(Long userId) {
        List<Object> dateObjects = breathingRepository.findDistinctActiveDatesByUserId(userId);
        if (dateObjects == null || dateObjects.isEmpty()) {
            return 0;
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDate expected = today;
        int streak = 0;

        for (int i = 0; i < dateObjects.size(); i++) {
            Object obj = dateObjects.get(i);
            LocalDate sessionDate;

            if (obj instanceof java.sql.Date) {
                sessionDate = ((java.sql.Date) obj).toLocalDate();
            } else if (obj instanceof LocalDate) {
                sessionDate = (LocalDate) obj;
            } else {
                sessionDate = LocalDate.parse(obj.toString());
            }

            if (i == 0) {
                if (sessionDate.equals(today)) {
                    streak++;
                    expected = yesterday;
                } else if (sessionDate.equals(yesterday)) {
                    streak++;
                    expected = yesterday.minusDays(1);
                } else {
                    return 0; // Streak broken
                }
            } else {
                if (sessionDate.equals(expected)) {
                    streak++;
                    expected = expected.minusDays(1);
                } else {
                    break;
                }
            }
        }

        return streak;
    }
}
