package com.stressless.controller;

import com.stressless.dto.request.MoodEntryRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.dto.response.MoodTrendDto;
import com.stressless.entity.MoodEntry;
import com.stressless.service.MoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mood")
public class MoodTrackerController {

    @Autowired
    private MoodService moodService;

    @PostMapping
    public ResponseEntity<ApiResponse<MoodEntry>> logMood(@Valid @RequestBody MoodEntryRequest request) {
        MoodEntry saved = moodService.logMood(request);
        return ResponseEntity.ok(ApiResponse.ok("Mood logged successfully", saved));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<MoodEntry>>> getMoodHistory() {
        List<MoodEntry> history = moodService.getMoodHistory();
        return ResponseEntity.ok(ApiResponse.ok("Mood history retrieved", history));
    }

    @GetMapping("/trends")
    public ResponseEntity<ApiResponse<MoodTrendDto>> getMoodTrends(
            @RequestParam(defaultValue = "7") int days) {
        MoodTrendDto trends = moodService.getMoodTrends(days);
        return ResponseEntity.ok(ApiResponse.ok("Mood trends for last " + days + " days retrieved", trends));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMoodEntry(@PathVariable Long id) {
        moodService.deleteMoodEntry(id);
        return ResponseEntity.ok(ApiResponse.ok("Mood entry deleted"));
    }
}
