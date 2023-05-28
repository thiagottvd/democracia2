package com.example.democracia_desktop.models;

public class MessageModel {

    private String message;

    public MessageModel() {
        // Default constructor required for deserialization
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
