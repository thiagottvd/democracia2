package com.example.democracia_desktop.models;

public class PollModel {
    private String title;

    public PollModel() {
        /*
        PollModel class requires a default public constructor for proper deserialization because Jackson,
        the JSON serialization/deserialization library, relies on either a default constructor or setter
        methods to instantiate objects during the deserialization process.
        */
    }

    public PollModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

