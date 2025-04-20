package com.Nexus_Library.model;

import java.sql.Timestamp;

public class AudioBook extends LibraryItem {
    private int durationMinutes;

    public AudioBook(int itemId, String title, String author, String isbn, boolean isAvailable, Timestamp addedDate, int durationMinutes) {
        super(itemId, title, author, isbn, isAvailable, addedDate);
        this.durationMinutes = durationMinutes;
    }

    @Override
    public int getMaxBorrowDays() {
        return 14; // Audio books for 14 days
    }

    @Override
    public String getItemType() {
        return "AudioBook";
    }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
}