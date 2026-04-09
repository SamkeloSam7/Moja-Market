package com.example.mojamarket;

public class OnboardingItem {
    private final int iconResId;
    private final String title;
    private final String description;
    private final int backgroundResId;

    public OnboardingItem(int iconResId, String title, String description, int backgroundResId) {
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.backgroundResId = backgroundResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }
}