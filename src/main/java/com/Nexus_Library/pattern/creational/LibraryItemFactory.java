package com.Nexus_Library.pattern.creational;

import com.Nexus_Library.model.*;

import java.sql.Timestamp;

public class LibraryItemFactory {
    public static LibraryItem createItem(String type, int itemId, String title, String author, String isbn,
                                         boolean isAvailable, Timestamp addedDate, String extraParam) {
        switch (type.toLowerCase()) {
            case "ebook":
                return new EBook(itemId, title, author, isbn, isAvailable, addedDate, extraParam);
            case "physicalbook":
                return new PhysicalBook(itemId, title, author, isbn, isAvailable, addedDate, Integer.parseInt(extraParam));
            case "researchpaper":
                return new ResearchPaper(itemId, title, author, isbn, isAvailable, addedDate, extraParam);
            case "audiobook":
                return new AudioBook(itemId, title, author, isbn, isAvailable, addedDate, Integer.parseInt(extraParam));
            default:
                throw new IllegalArgumentException("Unknown item type: " + type);
        }
    }
}