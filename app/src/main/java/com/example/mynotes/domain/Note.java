package com.example.mynotes.domain;

import java.time.LocalDateTime;

public class Note {
    private String title;
    private String description;
    private LocalDateTime creationDate;

    public Note(String title, String description, LocalDateTime creationDate) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }


}
