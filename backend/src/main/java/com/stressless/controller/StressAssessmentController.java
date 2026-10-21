package com.stressless.controller;

import com.stressless.dto.request.StressAssessmentRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.dto.response.StressAssessmentResultDto;
import com.stressless.service.StressAssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class StressAssessmentController {

    @Autowired
    private StressAssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StressAssessmentResultDto>> submitAssessment(
            @Valid @RequestBody StressAssessmentRequest request) {
        StressAssessmentResultDto result = assessmentService.submitAssessment(request);
        return ResponseEntity.ok(ApiResponse.ok("Stress assessment evaluated successfully", result));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<StressAssessmentResultDto>>> getAssessmentHistory() {
        List<StressAssessmentResultDto> history = assessmentService.getUserAssessmentHistory();
        return ResponseEntity.ok(ApiResponse.ok("Assessment history retrieved", history));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<StressAssessmentResultDto>> getLatestAssessment() {
        return assessmentService.getLatestAssessment()
                .map(dto -> ResponseEntity.ok(ApiResponse.ok("Latest assessment found", dto)))
                .orElse(ResponseEntity.ok(ApiResponse.ok("No assessment recorded yet", null)));
    }
}
