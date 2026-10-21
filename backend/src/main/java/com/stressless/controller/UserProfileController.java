package com.stressless.controller;

import com.stressless.dto.request.UpdateProfileRequest;
import com.stressless.dto.response.ApiResponse;
import com.stressless.entity.MotivationalQuote;
import com.stressless.entity.User;
import com.stressless.service.MotivationalQuoteService;
import com.stressless.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private MotivationalQuoteService quoteService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getProfile() {
        User user = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("User profile", user));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<User>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User updated = userService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", updated));
    }

    @GetMapping("/quotes/daily")
    public ResponseEntity<ApiResponse<MotivationalQuote>> getDailyQuote() {
        MotivationalQuote quote = quoteService.getDailyQuote();
        return ResponseEntity.ok(ApiResponse.ok("Daily motivational quote", quote));
    }

    @GetMapping("/quotes/random")
    public ResponseEntity<ApiResponse<MotivationalQuote>> getRandomQuote() {
        MotivationalQuote quote = quoteService.getDailyQuote();
        return ResponseEntity.ok(ApiResponse.ok("Random motivational quote", quote));
    }
}
