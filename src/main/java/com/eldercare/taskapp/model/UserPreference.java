package com.eldercare.taskapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String fontSize = "large"; // normal, large, extra-large

    @Column(nullable = false, length = 30)
    private String theme = "cream"; // cream, dark, high-contrast

    @Column(nullable = false)
    private boolean speechEnabled = true;

    @Column(nullable = false)
    private double speechSpeed = 0.85;

    @Column(nullable = false)
    private boolean soundEffectsEnabled = true;

    @Column(nullable = false)
    private boolean simplifiedView = false;

    @Column(nullable = false)
    private boolean highContrast = false;

    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserPreference() {
    }

    public UserPreference(Long userId) {
        this.userId = userId;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isSpeechEnabled() {
        return speechEnabled;
    }

    public void setSpeechEnabled(boolean speechEnabled) {
        this.speechEnabled = speechEnabled;
    }

    public double getSpeechSpeed() {
        return speechSpeed;
    }

    public void setSpeechSpeed(double speechSpeed) {
        this.speechSpeed = speechSpeed;
    }

    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        this.soundEffectsEnabled = soundEffectsEnabled;
    }

    public boolean isSimplifiedView() {
        return simplifiedView;
    }

    public void setSimplifiedView(boolean simplifiedView) {
        this.simplifiedView = simplifiedView;
    }

    public boolean isHighContrast() {
        return highContrast;
    }

    public void setHighContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
