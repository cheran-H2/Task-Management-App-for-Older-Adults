package com.eldercare.taskapp.dto;

public class PreferenceUpdateRequest {
    private String fontSize;
    private String theme;
    private Boolean speechEnabled;
    private Double speechSpeed;
    private Boolean soundEffectsEnabled;
    private Boolean simplifiedView;
    private Boolean highContrast;

    public PreferenceUpdateRequest() {
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

    public Boolean getSpeechEnabled() {
        return speechEnabled;
    }

    public void setSpeechEnabled(Boolean speechEnabled) {
        this.speechEnabled = speechEnabled;
    }

    public Double getSpeechSpeed() {
        return speechSpeed;
    }

    public void setSpeechSpeed(Double speechSpeed) {
        this.speechSpeed = speechSpeed;
    }

    public Boolean getSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public void setSoundEffectsEnabled(Boolean soundEffectsEnabled) {
        this.soundEffectsEnabled = soundEffectsEnabled;
    }

    public Boolean getSimplifiedView() {
        return simplifiedView;
    }

    public void setSimplifiedView(Boolean simplifiedView) {
        this.simplifiedView = simplifiedView;
    }

    public Boolean getHighContrast() {
        return highContrast;
    }

    public void setHighContrast(Boolean highContrast) {
        this.highContrast = highContrast;
    }
}
