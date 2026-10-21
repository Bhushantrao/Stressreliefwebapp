package com.stressless.dto.request;

import com.stressless.entity.BreathingSession.Technique;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BreathingSessionRequest {

    @NotNull(message = "Technique is required")
    private Technique technique;

    @NotNull(message = "Duration in seconds is required")
    @Min(value = 10, message = "Duration must be at least 10 seconds")
    private Integer durationSeconds;

    @Min(1)
    private Integer cyclesCompleted = 1;

    public BreathingSessionRequest() {
    }

    public Technique getTechnique() {
        return technique;
    }

    public void setTechnique(Technique technique) {
        this.technique = technique;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getCyclesCompleted() {
        return cyclesCompleted;
    }

    public void setCyclesCompleted(Integer cyclesCompleted) {
        this.cyclesCompleted = cyclesCompleted;
    }
}
