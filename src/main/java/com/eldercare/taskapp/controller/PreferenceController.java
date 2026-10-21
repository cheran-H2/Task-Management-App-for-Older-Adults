package com.eldercare.taskapp.controller;

import com.eldercare.taskapp.dto.PreferenceUpdateRequest;
import com.eldercare.taskapp.model.UserPreference;
import com.eldercare.taskapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "*")
public class PreferenceController {

    private final UserService userService;

    public PreferenceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserPreference> getPreferences(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(userService.getPreferences(userId));
    }

    @PutMapping
    public ResponseEntity<UserPreference> updatePreferences(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestBody PreferenceUpdateRequest request) {
        return ResponseEntity.ok(userService.updatePreferences(userId, request));
    }
}
