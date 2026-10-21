package com.eldercare.taskapp.model;

public enum Category {
    MEDICATION("Medication & Pills", "💊"),
    APPOINTMENT("Doctor & Visits", "🩺"),
    HEALTH("Health & Exercise", "🚶"),
    ROUTINE("Home & Routine", "🏡"),
    SOCIAL("Family & Friends", "📞"),
    CHORE("Chores & Groceries", "🛒"),
    LEISURE("Relaxation & Leisure", "📖");

    private final String displayName;
    private final String emoji;

    Category(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }
}
