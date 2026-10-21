package com.eldercare.taskapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_activity_logs")
public class TaskActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String actionType; // TASK_CREATED, TASK_COMPLETED, TASK_UNCOMPLETED, REMINDER_ACKNOWLEDGED, VOICE_ASSIST_USED

    @Column(columnDefinition = "TEXT")
    private String actionDetails;

    @Column(nullable = false)
    private LocalDateTime loggedAt = LocalDateTime.now();

    public TaskActivityLog() {
    }

    public TaskActivityLog(Long taskId, Long userId, String actionType, String actionDetails) {
        this.taskId = taskId;
        this.userId = userId;
        this.actionType = actionType;
        this.actionDetails = actionDetails;
        this.loggedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
