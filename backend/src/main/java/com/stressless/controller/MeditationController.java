package com.stressless.controller;

import com.stressless.dto.request.MeditationCompletionRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.entity.MeditationSession;
import com.stressless.entity.UserMeditationCompletion;
import com.stressless.service.MeditationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meditations")
public class MeditationController {

    @Autowired
    private MeditationService meditationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MeditationSession>>> getSessions(
            @RequestParam(required = false) String category) {
        List<MeditationSession> sessions = meditationService.getAllSessions(category);
        return ResponseEntity.ok(ApiResponse.ok("Meditation sessions retrieved", sessions));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<MeditationSession>>> getFeaturedSessions() {
        List<MeditationSession> sessions = meditationService.getAllSessions(null);
        List<MeditationSession> featured = sessions.subList(0, Math.min(3, sessions.size()));
        return ResponseEntity.ok(ApiResponse.ok("Featured meditation sessions retrieved", featured));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MeditationSession>> getSessionById(@PathVariable Long id) {
        MeditationSession session = meditationService.getSessionById(id);
        return ResponseEntity.ok(ApiResponse.ok("Session details retrieved", session));
    }

    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<UserMeditationCompletion>> recordCompletion(
            @Valid @RequestBody MeditationCompletionRequest request) {
        UserMeditationCompletion completion = meditationService.recordCompletion(request);
        return ResponseEntity.ok(ApiResponse.ok("Meditation session completed! Great job taking time for yourself.", completion));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<UserMeditationCompletion>>> getCompletionHistory() {
        List<UserMeditationCompletion> history = meditationService.getUserCompletionHistory();
        return ResponseEntity.ok(ApiResponse.ok("Meditation history retrieved", history));
    }
}
