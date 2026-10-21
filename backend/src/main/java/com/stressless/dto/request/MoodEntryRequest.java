package com.stressless.dto.request;

import com.stressless.entity.MoodEntry.MoodType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MoodEntryRequest {

    @NotNull(message = "Mood is required")
    private MoodType mood;

    @Min(1) @Max(5)
    private Integer energyLevel = 3;

    private String notes;
    private String tags;

    public MoodEntryRequest() {
    }

    public MoodType getMood() {
        return mood;
    }

    public void setMood(MoodType mood) {
        this.mood = mood;
    }

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
