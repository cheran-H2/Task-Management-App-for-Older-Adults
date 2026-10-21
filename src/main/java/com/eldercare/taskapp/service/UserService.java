package com.eldercare.taskapp.service;

import com.eldercare.taskapp.dto.AuthResponse;
import com.eldercare.taskapp.dto.LoginRequest;
import com.eldercare.taskapp.dto.PreferenceUpdateRequest;
import com.eldercare.taskapp.dto.RegisterRequest;
import com.eldercare.taskapp.model.User;
import com.eldercare.taskapp.model.UserPreference;
import com.eldercare.taskapp.repository.UserPreferenceRepository;
import com.eldercare.taskapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository preferenceRepository;

    public UserService(UserRepository userRepository, UserPreferenceRepository preferenceRepository) {
        this.userRepository = userRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return AuthResponse.failure("Please enter your email address.");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return AuthResponse.failure("Please enter your password.");
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) {
            return AuthResponse.failure("We could not find an account with that email. Please check the spelling or sign up.");
        }

        User user = userOpt.get();
        // In this accessible demo, passwords match directly
        if (!user.getPassword().equals(request.getPassword().trim())) {
            return AuthResponse.failure("Incorrect password. Please try again or ask your caregiver for assistance.");
        }

        UserPreference prefs = preferenceRepository.findByUserId(user.getId())
                .orElseGet(() -> createDefaultPreferences(user));

        return AuthResponse.success(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getEmergencyContactName(),
                user.getEmergencyContactPhone(),
                prefs
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            return AuthResponse.failure("Please provide your full name.");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return AuthResponse.failure("Please provide an email address.");
        }
        if (request.getPassword() == null || request.getPassword().trim().length() < 4) {
            return AuthResponse.failure("Please choose a password with at least 4 characters.");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            return AuthResponse.failure("An account with this email already exists. Try logging in.");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPassword(request.getPassword().trim());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmergencyContactName(request.getEmergencyContactName());
        user.setEmergencyContactPhone(request.getEmergencyContactPhone());

        user = userRepository.save(user);
        UserPreference prefs = createDefaultPreferences(user);

        return AuthResponse.success(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getEmergencyContactName(),
                user.getEmergencyContactPhone(),
                prefs
        );
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public UserPreference getPreferences(Long userId) {
        return preferenceRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    return createDefaultPreferences(user);
                });
    }

    @Transactional
    public UserPreference updatePreferences(Long userId, PreferenceUpdateRequest req) {
        UserPreference prefs = getPreferences(userId);
        if (req.getFontSize() != null) {
            prefs.setFontSize(req.getFontSize());
        }
        if (req.getTheme() != null) {
            prefs.setTheme(req.getTheme());
        }
        if (req.getSpeechEnabled() != null) {
            prefs.setSpeechEnabled(req.getSpeechEnabled());
        }
        if (req.getSpeechSpeed() != null) {
            prefs.setSpeechSpeed(req.getSpeechSpeed());
        }
        if (req.getSoundEffectsEnabled() != null) {
            prefs.setSoundEffectsEnabled(req.getSoundEffectsEnabled());
        }
        if (req.getSimplifiedView() != null) {
            prefs.setSimplifiedView(req.getSimplifiedView());
        }
        if (req.getHighContrast() != null) {
            prefs.setHighContrast(req.getHighContrast());
        }
        return preferenceRepository.save(prefs);
    }

    private UserPreference createDefaultPreferences(User user) {
        UserPreference prefs = new UserPreference(user.getId());
        prefs.setFontSize("large");
        prefs.setTheme("cream");
        prefs.setSpeechEnabled(true);
        prefs.setSpeechSpeed(0.85);
        prefs.setSoundEffectsEnabled(true);
        prefs.setSimplifiedView(false);
        return preferenceRepository.save(prefs);
    }
}
