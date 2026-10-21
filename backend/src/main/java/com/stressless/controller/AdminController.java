package com.stressless.controller;

import com.stressless.dto.request.MeditationSessionRequest;
import com.stressless.dto.request.QuoteRequest;
import com.stressless.dto.request.RelaxationResourceRequest;
import com.stressless.dto.response.AdminStatsDto;
import com.stressless.dto.response.ApiResponse;
import com.stressless.entity.MeditationSession;
import com.stressless.entity.MotivationalQuote;
import com.stressless.entity.RelaxationResource;
import com.stressless.entity.User;
import com.stressless.repository.*;
import com.stressless.service.MeditationService;
import com.stressless.service.MotivationalQuoteService;
import com.stressless.service.RelaxationResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeditationSessionRepository meditationRepository;

    @Autowired
    private MotivationalQuoteRepository quoteRepository;

    @Autowired
    private RelaxationResourceRepository resourceRepository;

    @Autowired
    private BreathingSessionRepository breathingRepository;

    @Autowired
    private UserMeditationCompletionRepository completionRepository;

    @Autowired
    private MoodEntryRepository moodRepository;

    @Autowired
    private JournalEntryRepository journalRepository;

    @Autowired
    private MotivationalQuoteService quoteService;

    @Autowired
    private MeditationService meditationService;

    @Autowired
    private RelaxationResourceService resourceService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsDto>> getSystemStats() {
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalMeditationSessions(meditationRepository.count());
        stats.setTotalQuotes(quoteRepository.count());
        stats.setTotalRelaxationResources(resourceRepository.count());
        stats.setTotalBreathingSessionsCompleted(breathingRepository.count());
        stats.setTotalMeditationCompletions(completionRepository.count());
        stats.setTotalMoodLogs(moodRepository.count());
        stats.setTotalJournalEntries(journalRepository.count());

        return ResponseEntity.ok(ApiResponse.ok("System statistics retrieved", stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.ok("User list retrieved", users));
    }

    // --- Quotes Management ---
    @GetMapping("/quotes")
    public ResponseEntity<ApiResponse<List<MotivationalQuote>>> getAllQuotes() {
        return ResponseEntity.ok(ApiResponse.ok("All quotes", quoteService.getAllQuotes()));
    }

    @PostMapping("/quotes")
    public ResponseEntity<ApiResponse<MotivationalQuote>> createQuote(@Valid @RequestBody QuoteRequest request) {
        MotivationalQuote quote = quoteService.createQuote(request);
        return ResponseEntity.ok(ApiResponse.ok("Quote created successfully", quote));
    }

    @PutMapping("/quotes/{id}")
    public ResponseEntity<ApiResponse<MotivationalQuote>> updateQuote(
            @PathVariable Long id, @Valid @RequestBody QuoteRequest request) {
        MotivationalQuote quote = quoteService.updateQuote(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Quote updated successfully", quote));
    }

    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return ResponseEntity.ok(ApiResponse.ok("Quote deleted successfully"));
    }

    // --- Meditation Sessions Management ---
    @PostMapping("/meditations")
    public ResponseEntity<ApiResponse<MeditationSession>> createMeditation(
            @Valid @RequestBody MeditationSessionRequest request) {
        MeditationSession session = meditationService.createSession(request);
        return ResponseEntity.ok(ApiResponse.ok("Meditation session created", session));
    }

    @PutMapping("/meditations/{id}")
    public ResponseEntity<ApiResponse<MeditationSession>> updateMeditation(
            @PathVariable Long id, @Valid @RequestBody MeditationSessionRequest request) {
        MeditationSession session = meditationService.updateSession(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Meditation session updated", session));
    }

    @DeleteMapping("/meditations/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMeditation(@PathVariable Long id) {
        meditationService.deleteSession(id);
        return ResponseEntity.ok(ApiResponse.ok("Meditation session deleted"));
    }

    // --- Relaxation Resources Management ---
    @PostMapping("/music")
    public ResponseEntity<ApiResponse<RelaxationResource>> createRelaxationResource(
            @Valid @RequestBody RelaxationResourceRequest request) {
        RelaxationResource resource = resourceService.createResource(request);
        return ResponseEntity.ok(ApiResponse.ok("Relaxation sound created", resource));
    }

    @PutMapping("/music/{id}")
    public ResponseEntity<ApiResponse<RelaxationResource>> updateRelaxationResource(
            @PathVariable Long id, @Valid @RequestBody RelaxationResourceRequest request) {
        RelaxationResource resource = resourceService.updateResource(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Relaxation sound updated", resource));
    }

    @DeleteMapping("/music/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRelaxationResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok(ApiResponse.ok("Relaxation sound deleted"));
    }
}
