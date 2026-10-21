package com.stressless.controller;

import com.stressless.dto.request.WellnessGoalRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.dto.response.ProgressReportDto;
import com.stressless.entity.WellnessGoal;
import com.stressless.service.DashboardService;
import com.stressless.service.WellnessGoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProgressController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private WellnessGoalService goalService;

    @GetMapping("/progress/report")
    public ResponseEntity<ApiResponse<ProgressReportDto>> getProgressReport() {
        ProgressReportDto report = dashboardService.getProgressReport();
        return ResponseEntity.ok(ApiResponse.ok("Progress report retrieved", report));
    }

    @GetMapping("/goals")
    public ResponseEntity<ApiResponse<List<WellnessGoal>>> getGoals() {
        List<WellnessGoal> goals = goalService.getUserGoals();
        return ResponseEntity.ok(ApiResponse.ok("User wellness goals retrieved", goals));
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse<WellnessGoal>> createGoal(@Valid @RequestBody WellnessGoalRequest request) {
        WellnessGoal goal = goalService.createGoal(request);
        return ResponseEntity.ok(ApiResponse.ok("Wellness goal created successfully", goal));
    }

    @PatchMapping("/goals/{id}/progress")
    public ResponseEntity<ApiResponse<WellnessGoal>> updateGoalProgress(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int amount) {
        WellnessGoal goal = goalService.updateGoalProgress(id, amount);
        return ResponseEntity.ok(ApiResponse.ok("Goal progress updated", goal));
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.ok(ApiResponse.ok("Wellness goal removed"));
    }
}
