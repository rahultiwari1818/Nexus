package com.Nexus_Library.model;

public class Resource {
    private int resourceId;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String resourceType;
    private String status;
    private String location;

    public Resource(int resourceId, String title, String author, String publisher, String isbn, String resourceType, String status, String location) {
        this.resourceId = resourceId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.resourceType = resourceType;
        this.status = status;
        this.location = location;
    }

    // Getters
    public int getResourceId() { return resourceId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getIsbn() { return isbn; }
    public String getResourceType() { return resourceType; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return "Resource{resourceId=" + resourceId + ", title='" + title + "', author='" + author + "'}";
    }
}

//package com.Nexus_Library.model;
//
//public class Resource {
//    private int resourceId;
//    private String title, author, publisher, isbn, resourceType, status, location;
//
//    public Resource(int resourceId, String title, String author, String publisher, String isbn, String resourceType, String status, String location) {
//        this.resourceId = resourceId;
//        this.title = title;
//        this.author = author;
//        this.publisher = publisher;
//        this.isbn = isbn;
//        this.resourceType = resourceType;
//        this.status = status;
//        this.location = location;
//    }
//
//    // Getters and setters
//
//    public int getResourceId() {
//        return resourceId;
//    }
//
//    public void setResourceId(int resourceId) {
//        this.resourceId = resourceId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getPublisher() {
//        return publisher;
//    }
//
//    public void setPublisher(String publisher) {
//        this.publisher = publisher;
//    }
//
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public String getResourceType() {
//        return resourceType;
//    }
//
//    public void setResourceType(String resourceType) {
//        this.resourceType = resourceType;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//}
