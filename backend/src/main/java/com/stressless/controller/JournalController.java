package com.stressless.controller;

import com.stressless.dto.request.JournalEntryRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.entity.JournalEntry;
import com.stressless.service.JournalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getJournals(
            @RequestParam(required = false) String search) {
        List<JournalEntry> entries = journalService.getAllUserJournals(search);
        return ResponseEntity.ok(ApiResponse.ok("Journal entries retrieved", entries));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalEntry>> getJournalById(@PathVariable Long id) {
        JournalEntry entry = journalService.getJournalById(id);
        return ResponseEntity.ok(ApiResponse.ok("Journal entry details", entry));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JournalEntry>> createJournal(
            @Valid @RequestBody JournalEntryRequest request) {
        JournalEntry created = journalService.createJournal(request);
        return ResponseEntity.ok(ApiResponse.ok("Journal entry created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalEntry>> updateJournal(
            @PathVariable Long id,
            @Valid @RequestBody JournalEntryRequest request) {
        JournalEntry updated = journalService.updateJournal(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Journal entry updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJournal(@PathVariable Long id) {
        journalService.deleteJournal(id);
        return ResponseEntity.ok(ApiResponse.ok("Journal entry deleted successfully"));
    }
}
