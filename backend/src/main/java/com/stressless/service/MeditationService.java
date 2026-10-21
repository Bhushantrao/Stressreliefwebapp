package com.stressless.service;

import com.stressless.dto.request.MeditationCompletionRequest;
import com.stressless.dto.request.MeditationSessionRequest;
import com.stressless.entity.MeditationSession;
import com.stressless.entity.User;
import com.stressless.entity.UserMeditationCompletion;
import com.stressless.entity.WellnessGoal.GoalTargetType;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.repository.MeditationSessionRepository;
import com.stressless.repository.UserMeditationCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeditationService {

    @Autowired
    private MeditationSessionRepository meditationRepository;

    @Autowired
    private UserMeditationCompletionRepository completionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WellnessGoalService wellnessGoalService;

    public List<MeditationSession> getAllSessions(String category) {
        if (category != null && !category.trim().equalsIgnoreCase("ALL") && !category.trim().isEmpty()) {
            return meditationRepository.findByCategoryIgnoreCase(category.trim());
        }
        return meditationRepository.findAllByOrderByCreatedAtDesc();
    }

    public MeditationSession getSessionById(Long id) {
        return meditationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meditation session not found with id: " + id));
    }

    @Transactional
    public UserMeditationCompletion recordCompletion(MeditationCompletionRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        MeditationSession session = getSessionById(request.getMeditationSessionId());

        int durationSeconds = (request.getDurationSeconds() != null && request.getDurationSeconds() > 0)
                ? request.getDurationSeconds()
                : (session.getDurationMinutes() * 60);

        UserMeditationCompletion completion = new UserMeditationCompletion(user, session, durationSeconds);
        UserMeditationCompletion saved = completionRepository.save(completion);

        // Update wellness goals for meditation minutes
        int minutes = (int) Math.ceil(durationSeconds / 60.0);
        wellnessGoalService.incrementGoalProgress(user.getId(), GoalTargetType.MEDITATION_MINUTES, minutes);

        return saved;
    }

    public List<UserMeditationCompletion> getUserCompletionHistory() {
        User user = userService.getCurrentAuthenticatedUser();
        return completionRepository.findByUserIdOrderByCompletedAtDesc(user.getId());
    }

    @Transactional
    public MeditationSession createSession(MeditationSessionRequest request) {
        MeditationSession session = new MeditationSession();
        session.setTitle(request.getTitle().trim());
        session.setDescription(request.getDescription().trim());
        session.setCategory(request.getCategory() != null ? request.getCategory().trim() : "Mindfulness");
        session.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 5);
        session.setAudioUrl(request.getAudioUrl());
        session.setImageUrl(request.getImageUrl());
        session.setDifficulty(request.getDifficulty());
        session.setInstructions(request.getInstructions());

        return meditationRepository.save(session);
    }

    @Transactional
    public MeditationSession updateSession(Long id, MeditationSessionRequest request) {
        MeditationSession session = getSessionById(id);

        session.setTitle(request.getTitle().trim());
        session.setDescription(request.getDescription().trim());
        session.setCategory(request.getCategory() != null ? request.getCategory().trim() : "Mindfulness");
        session.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 5);
        session.setAudioUrl(request.getAudioUrl());
        session.setImageUrl(request.getImageUrl());
        session.setDifficulty(request.getDifficulty());
        session.setInstructions(request.getInstructions());

        return meditationRepository.save(session);
    }

    @Transactional
    public void deleteSession(Long id) {
        if (!meditationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meditation session not found with id: " + id);
        }
        meditationRepository.deleteById(id);
    }
}
