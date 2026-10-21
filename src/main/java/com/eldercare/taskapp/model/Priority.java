package com.eldercare.taskapp.model;

public enum Priority {
    HIGH("High (Urgent / Health)", "#DC2626", "heart-pulse"),
    MEDIUM("Medium (Routine / Chores)", "#D97706", "clock"),
    LOW("Low (Social / Leisure)", "#2563EB", "sparkles");

    private final String label;
    private final String colorHex;
    private final String icon;

    Priority(String label, String colorHex, String icon) {
        this.label = label;
        this.colorHex = colorHex;
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public String getColorHex() {
        return colorHex;
    }

    public String getIcon() {
        return icon;
    }
}
