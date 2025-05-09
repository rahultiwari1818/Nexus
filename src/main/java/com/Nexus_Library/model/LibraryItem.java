package com.Nexus_Library.model;

import java.sql.Timestamp;

public abstract class LibraryItem {
    private int itemId;
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private Timestamp addedDate;
    private String extraParam;  // for things like page count, narrator, volume, etc.
    private String type;        // "EBook", "PhysicalBook", etc.

    public LibraryItem(int itemId, String title, String author, String isbn, boolean isAvailable,
                       Timestamp addedDate) {
        this.itemId = itemId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.addedDate = addedDate;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public Timestamp getAddedDate() { return addedDate; }
    public void setAddedDate(Timestamp addedDate) { this.addedDate = addedDate; }

    public String getExtraParam() { return extraParam; }
    public void setExtraParam(String extraParam) { this.extraParam = extraParam; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // Abstract methods to be implemented by subclasses
    public abstract int getMaxBorrowDays();
    public abstract String getItemType();
}
