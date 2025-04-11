package com.Nexus_Library.model;

public class Metadata {
    private int metadataId, resourceId;
    private String keywords, tags;
    private double rating;

    public Metadata(int metadataId, int resourceId, String keywords, String tags, double rating) {
        this.metadataId = metadataId;
        this.resourceId = resourceId;
        this.keywords = keywords;
        this.tags = tags;
        this.rating = rating;
    }

    // Getters and setters

    public int getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(int metadataId) {
        this.metadataId = metadataId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
