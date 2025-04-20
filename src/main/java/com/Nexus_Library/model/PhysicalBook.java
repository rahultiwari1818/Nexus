package com.Nexus_Library.model;

import java.sql.Timestamp;

public class PhysicalBook extends LibraryItem {
    private int copies;

    public PhysicalBook(int itemId, String title, String author, String isbn, boolean isAvailable, Timestamp addedDate, int copies) {
        super(itemId, title, author, isbn, isAvailable, addedDate);
        this.copies = copies;
    }

    @Override
    public int getMaxBorrowDays() {
        return 21; // Physical books for 21 days
    }

    @Override
    public String getItemType() {
        return "PhysicalBook";
    }

    public int getCopies() { return copies; }
    public void setCopies(int copies) { this.copies = copies; }
}