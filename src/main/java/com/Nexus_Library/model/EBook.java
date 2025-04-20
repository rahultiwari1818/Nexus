package com.Nexus_Library.model;

import java.sql.Timestamp;

public class EBook extends LibraryItem {
    private String fileFormat;

    public EBook(int itemId, String title, String author, String isbn, boolean isAvailable, Timestamp addedDate, String fileFormat) {
        super(itemId, title, author, isbn, isAvailable, addedDate);
        this.fileFormat = fileFormat;
    }

    @Override
    public int getMaxBorrowDays() {
        return 14; // EBooks can be borrowed for 14 days
    }

    @Override
    public String getItemType() {
        return "EBook";
    }

    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }
}