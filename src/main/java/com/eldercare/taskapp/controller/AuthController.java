package com.eldercare.taskapp.controller;

import com.eldercare.taskapp.dto.AuthResponse;
import com.eldercare.taskapp.dto.LoginRequest;
import com.eldercare.taskapp.dto.RegisterRequest;
import com.eldercare.taskapp.model.User;
import com.eldercare.taskapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestParam(defaultValue = "1") Long userId) {
        return userService.findById(userId)
                .map(u -> {
                    var prefs = userService.getPreferences(u.getId());
                    return ResponseEntity.ok(AuthResponse.success(
                            u.getId(),
                            u.getFullName(),
                            u.getEmail(),
                            u.getPhoneNumber(),
                            u.getEmergencyContactName(),
                            u.getEmergencyContactPhone(),
                            prefs
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
