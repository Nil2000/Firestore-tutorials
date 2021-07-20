package com.example.firebasefirestorebasicsapplication;

public class Note {
    private String title,description,documentId;

    public Note() {
        //Public no arg empty constructor
    }

    public Note(String title, String description, String documentId) {
        this.title = title;
        this.description = description;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
