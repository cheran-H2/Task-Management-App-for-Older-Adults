package com.eldercare.taskapp.dto;

import com.eldercare.taskapp.model.UserPreference;

public class AuthResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private UserPreference preferences;

    public AuthResponse() {
    }

    public static AuthResponse success(Long userId, String fullName, String email, String phone, 
                                       String emergencyName, String emergencyPhone, UserPreference prefs) {
        AuthResponse res = new AuthResponse();
        res.success = true;
        res.message = "Welcome, " + fullName + "!";
        res.userId = userId;
        res.fullName = fullName;
        res.email = email;
        res.phoneNumber = phone;
        res.emergencyContactName = emergencyName;
        res.emergencyContactPhone = emergencyPhone;
        res.preferences = prefs;
        return res;
    }

    public static AuthResponse failure(String message) {
        AuthResponse res = new AuthResponse();
        res.success = false;
        res.message = message;
        return res;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public UserPreference getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreference preferences) {
        this.preferences = preferences;
    }
}
