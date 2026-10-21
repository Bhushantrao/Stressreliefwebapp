package com.stressless.dto.request;

import com.stressless.entity.WellnessGoal.GoalPeriod;
import com.stressless.entity.WellnessGoal.GoalTargetType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class WellnessGoalRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Target type is required")
    private GoalTargetType targetType;

    @NotNull(message = "Target value is required")
    @Min(value = 1, message = "Target value must be at least 1")
    private Integer targetValue;

    private Integer currentValue = 0;

    private GoalPeriod period = GoalPeriod.DAILY;

    @NotNull(message = "Target date is required")
    private LocalDate targetDate;

    public WellnessGoalRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GoalTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(GoalTargetType targetType) {
        this.targetType = targetType;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public GoalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(GoalPeriod period) {
        this.period = period;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
}
