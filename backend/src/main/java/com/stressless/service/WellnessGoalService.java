package com.stressless.service;

import com.stressless.dto.request.WellnessGoalRequest;
import com.stressless.entity.User;
import com.stressless.entity.WellnessGoal;
import com.stressless.entity.WellnessGoal.GoalTargetType;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.exception.UnauthorizedAccessException;
import com.stressless.repository.WellnessGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WellnessGoalService {

    @Autowired
    private WellnessGoalRepository goalRepository;

    @Autowired
    private UserService userService;

    public List<WellnessGoal> getUserGoals() {
        User user = userService.getCurrentAuthenticatedUser();
        return goalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public List<WellnessGoal> getActiveGoals(Long userId) {
        return goalRepository.findByUserIdAndCompletedFalse(userId);
    }

    @Transactional
    public WellnessGoal createGoal(WellnessGoalRequest request) {
        User user = userService.getCurrentAuthenticatedUser();

        WellnessGoal goal = new WellnessGoal();
        goal.setUser(user);
        goal.setTitle(request.getTitle().trim());
        goal.setTargetType(request.getTargetType());
        goal.setTargetValue(request.getTargetValue());
        goal.setCurrentValue(request.getCurrentValue() != null ? request.getCurrentValue() : 0);
        goal.setPeriod(request.getPeriod());
        goal.setTargetDate(request.getTargetDate());
        goal.setCompleted(goal.getCurrentValue() >= goal.getTargetValue());

        return goalRepository.save(goal);
    }

    @Transactional
    public WellnessGoal updateGoalProgress(Long id, int addAmount) {
        User user = userService.getCurrentAuthenticatedUser();
        WellnessGoal goal = goalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + id));

        int newCurrent = Math.max(0, goal.getCurrentValue() + addAmount);
        goal.setCurrentValue(newCurrent);
        if (newCurrent >= goal.getTargetValue()) {
            goal.setCompleted(true);
        }

        return goalRepository.save(goal);
    }

    @Transactional
    public void incrementGoalProgress(Long userId, GoalTargetType type, int amount) {
        List<WellnessGoal> activeGoals = goalRepository.findByUserIdAndCompletedFalse(userId);
        for (WellnessGoal g : activeGoals) {
            if (g.getTargetType() == type) {
                int newCurrent = g.getCurrentValue() + amount;
                g.setCurrentValue(newCurrent);
                if (newCurrent >= g.getTargetValue()) {
                    g.setCompleted(true);
                }
                goalRepository.save(g);
            }
        }
    }

    @Transactional
    public void deleteGoal(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        WellnessGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + id));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized to delete this goal");
        }

        goalRepository.delete(goal);
    }
}
