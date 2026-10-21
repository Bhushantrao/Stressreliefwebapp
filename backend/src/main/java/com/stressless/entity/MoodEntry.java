package com.stressless.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mood_entries")
public class MoodEntry {

    public enum MoodType {
        HAPPY,
        CALM,
        NEUTRAL,
        ANXIOUS,
        SAD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MoodType mood;

    @Column(name = "energy_level")
    private Integer energyLevel = 3;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 255)
    private String tags;

    @Column(name = "logged_at", updatable = false)
    private LocalDateTime loggedAt = LocalDateTime.now();

    public MoodEntry() {
    }

    public MoodEntry(User user, MoodType mood, Integer energyLevel, String notes, String tags) {
        this.user = user;
        this.mood = mood;
        this.energyLevel = energyLevel != null ? energyLevel : 3;
        this.notes = notes;
        this.tags = tags;
        this.loggedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
