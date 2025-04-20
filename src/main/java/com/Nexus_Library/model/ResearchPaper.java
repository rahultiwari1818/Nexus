package com.Nexus_Library.model;

import java.sql.Timestamp;

public class ResearchPaper extends LibraryItem {
    private String publication;

    public ResearchPaper(int itemId, String title, String author, String isbn, boolean isAvailable, Timestamp addedDate, String publication) {
        super(itemId, title, author, isbn, isAvailable, addedDate);
        this.publication = publication;
    }

    @Override
    public int getMaxBorrowDays() {
        return 30; // Research papers for 30 days
    }

    @Override
    public String getItemType() {
        return "ResearchPaper";
    }

    public String getPublication() { return publication; }
    public void setPublication(String publication) { this.publication = publication; }
}