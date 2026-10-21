package com.stressless.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "relaxation_resources")
public class RelaxationResource {

    public enum Category {
        RAIN,
        OCEAN,
        FOREST,
        NIGHT,
        WHITE_NOISE,
        MEDITATION_MUSIC,
        PIANO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @NotBlank
    @Column(name = "audio_url", nullable = false, length = 500)
    private String audioUrl;

    @Column(name = "icon_name", length = 50)
    private String iconName = "music";

    @Column(length = 255)
    private String description;

    @Column(name = "duration_seconds")
    private Integer durationSeconds = 1800;

    @Column(name = "is_featured")
    private boolean isFeatured = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RelaxationResource() {
    }

    public RelaxationResource(String title, Category category, String audioUrl, String iconName, String description) {
        this.title = title;
        this.category = category;
        this.audioUrl = audioUrl;
        this.iconName = iconName;
        this.description = description;
        this.isFeatured = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
