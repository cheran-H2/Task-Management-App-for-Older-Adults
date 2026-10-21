package com.eldercare.taskapp.controller;

import com.eldercare.taskapp.dto.TaskRequest;
import com.eldercare.taskapp.dto.TaskStatsDto;
import com.eldercare.taskapp.model.Task;
import com.eldercare.taskapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String filter,
            @RequestParam(required = false) String priority) {
        List<Task> tasks = taskService.getTasks(userId, filter, priority);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Long userId) {
        return taskService.getTaskById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest request) {
        try {
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Task title cannot be empty."));
            }
            Task created = taskService.createTask(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request) {
        try {
            Task updated = taskService.updateTask(id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleComplete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Long userId) {
        try {
            Task toggled = taskService.toggleComplete(id, userId);
            return ResponseEntity.ok(toggled);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Long userId) {
        boolean deleted = taskService.deleteTask(id, userId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Task deleted successfully."));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsDto> getStats(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(taskService.getStats(userId));
    }

    @GetMapping("/reminders")
    public ResponseEntity<List<Task>> getTodayReminders(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(taskService.getTodayActiveReminders(userId));
    }
}
