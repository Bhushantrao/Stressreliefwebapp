package com.stressless.service;

import com.stressless.dto.request.JournalEntryRequest;
import com.stressless.entity.JournalEntry;
import com.stressless.entity.User;
import com.stressless.entity.WellnessGoal.GoalTargetType;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.exception.UnauthorizedAccessException;
import com.stressless.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalEntryRepository journalRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WellnessGoalService wellnessGoalService;

    public List<JournalEntry> getAllUserJournals(String search) {
        User user = userService.getCurrentAuthenticatedUser();
        if (search != null && !search.trim().isEmpty()) {
            return journalRepository.searchUserJournals(user.getId(), search.trim());
        }
        return journalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public JournalEntry getJournalById(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        return journalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found or unauthorized: " + id));
    }

    @Transactional
    public JournalEntry createJournal(JournalEntryRequest request) {
        User user = userService.getCurrentAuthenticatedUser();

        JournalEntry entry = new JournalEntry(
                user,
                request.getTitle().trim(),
                request.getContent().trim(),
                request.getMoodTag(),
                request.getTags()
        );

        JournalEntry saved = journalRepository.save(entry);

        // Update journal goals
        wellnessGoalService.incrementGoalProgress(user.getId(), GoalTargetType.JOURNAL_ENTRIES, 1);

        return saved;
    }

    @Transactional
    public JournalEntry updateJournal(Long id, JournalEntryRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        JournalEntry entry = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + id));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not permitted to modify another user's private journal");
        }

        entry.setTitle(request.getTitle().trim());
        entry.setContent(request.getContent().trim());
        entry.setMoodTag(request.getMoodTag());
        entry.setTags(request.getTags());

        return journalRepository.save(entry);
    }

    @Transactional
    public void deleteJournal(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        JournalEntry entry = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + id));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not permitted to delete another user's private journal");
        }

        journalRepository.delete(entry);
    }
}
