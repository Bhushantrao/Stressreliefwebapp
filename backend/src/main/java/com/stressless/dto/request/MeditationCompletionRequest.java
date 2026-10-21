package com.stressless.dto.request;

import jakarta.validation.constraints.NotNull;

public class MeditationCompletionRequest {

    @NotNull(message = "Meditation session ID is required")
    private Long meditationSessionId;

    private Integer durationSeconds;

    public MeditationCompletionRequest() {
    }

    public Long getMeditationSessionId() {
        return meditationSessionId;
    }

    public void setMeditationSessionId(Long meditationSessionId) {
        this.meditationSessionId = meditationSessionId;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
