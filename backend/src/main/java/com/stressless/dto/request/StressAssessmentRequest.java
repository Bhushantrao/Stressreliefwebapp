package com.stressless.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StressAssessmentRequest {

    @NotNull
    @Min(1) @Max(5)
    private Integer sleepRating; // 1 = Poor, 5 = Excellent

    @NotNull
    @Min(1) @Max(5)
    private Integer workloadRating; // 1 = Very manageable, 5 = Overwhelming

    @NotNull
    @Min(1) @Max(5)
    private Integer moodRating; // 1 = Low/Depressed, 5 = Calm/Joyful

    @NotNull
    @Min(1) @Max(5)
    private Integer tensionRating; // 1 = Relaxed, 5 = Intense physical tension

    @NotNull
    @Min(1) @Max(5)
    private Integer concentrationRating; // 1 = Distracted/Foggy, 5 = Laser focused

    @NotNull
    @Min(1) @Max(5)
    private Integer energyRating; // 1 = Exhausted, 5 = Vibrant/Energized

    private String notes;

    public StressAssessmentRequest() {
    }

    public Integer getSleepRating() {
        return sleepRating;
    }

    public void setSleepRating(Integer sleepRating) {
        this.sleepRating = sleepRating;
    }

    public Integer getWorkloadRating() {
        return workloadRating;
    }

    public void setWorkloadRating(Integer workloadRating) {
        this.workloadRating = workloadRating;
    }

    public Integer getMoodRating() {
        return moodRating;
    }

    public void setMoodRating(Integer moodRating) {
        this.moodRating = moodRating;
    }

    public Integer getTensionRating() {
        return tensionRating;
    }

    public void setTensionRating(Integer tensionRating) {
        this.tensionRating = tensionRating;
    }

    public Integer getConcentrationRating() {
        return concentrationRating;
    }

    public void setConcentrationRating(Integer concentrationRating) {
        this.concentrationRating = concentrationRating;
    }

    public Integer getEnergyRating() {
        return energyRating;
    }

    public void setEnergyRating(Integer energyRating) {
        this.energyRating = energyRating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
