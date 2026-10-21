package com.stressless.controller;

import com.stressless.dto.request.BreathingSessionRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.entity.BreathingSession;
import com.stressless.service.BreathingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breathing")
public class BreathingController {

    @Autowired
    private BreathingService breathingService;

    @PostMapping("/session")
    public ResponseEntity<ApiResponse<BreathingSession>> logBreathingSession(
            @Valid @RequestBody BreathingSessionRequest request) {
        BreathingSession session = breathingService.logSession(request);
        return ResponseEntity.ok(ApiResponse.ok("Breathing session logged successfully", session));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<BreathingSession>>> getBreathingHistory() {
        List<BreathingSession> history = breathingService.getUserBreathingHistory();
        return ResponseEntity.ok(ApiResponse.ok("Breathing history retrieved", history));
    }
}
