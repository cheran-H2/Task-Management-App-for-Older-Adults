package com.eldercare.taskapp.dto;

public class TaskStatsDto {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long todayTotal;
    private long todayCompleted;
    private long todayPending;
    private long highPriorityCount;
    private double overallCompletionRate;
    private double todayCompletionRate;
    private double usabilityTestingRate = 95.0; // 95% benchmark from usability study
    private String encouragingMessage;

    public TaskStatsDto() {
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public long getTodayTotal() {
        return todayTotal;
    }

    public void setTodayTotal(long todayTotal) {
        this.todayTotal = todayTotal;
    }

    public long getTodayCompleted() {
        return todayCompleted;
    }

    public void setTodayCompleted(long todayCompleted) {
        this.todayCompleted = todayCompleted;
    }

    public long getTodayPending() {
        return todayPending;
    }

    public void setTodayPending(long todayPending) {
        this.todayPending = todayPending;
    }

    public long getHighPriorityCount() {
        return highPriorityCount;
    }

    public void setHighPriorityCount(long highPriorityCount) {
        this.highPriorityCount = highPriorityCount;
    }

    public double getOverallCompletionRate() {
        return overallCompletionRate;
    }

    public void setOverallCompletionRate(double overallCompletionRate) {
        this.overallCompletionRate = overallCompletionRate;
    }

    public double getTodayCompletionRate() {
        return todayCompletionRate;
    }

    public void setTodayCompletionRate(double todayCompletionRate) {
        this.todayCompletionRate = todayCompletionRate;
    }

    public double getUsabilityTestingRate() {
        return usabilityTestingRate;
    }

    public void setUsabilityTestingRate(double usabilityTestingRate) {
        this.usabilityTestingRate = usabilityTestingRate;
    }

    public String getEncouragingMessage() {
        return encouragingMessage;
    }

    public void setEncouragingMessage(String encouragingMessage) {
        this.encouragingMessage = encouragingMessage;
    }
}
