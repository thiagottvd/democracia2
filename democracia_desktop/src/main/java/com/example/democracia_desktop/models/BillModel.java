package com.example.democracia_desktop.models;

import java.time.LocalDate;

public class BillModel {
    private Long id;
    private String title;
    private String description;
    private int numSupporters;
    private byte[] fileData;
    private LocalDate expirationDate;
    private String themeDesignation;
    private String delegateName;

    public BillModel() {
        // Default constructor required for deserialization
    }

    public BillModel(Long id, String title, String description, int numSupporters, byte[] fileData,
                     LocalDate expirationDate, String themeDesignation, String delegateName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.numSupporters = numSupporters;
        this.fileData = fileData;
        this.expirationDate = expirationDate;
        this.themeDesignation = themeDesignation;
        this.delegateName = delegateName;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNumSupporters() {
        return numSupporters;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getThemeDesignation() {
        return themeDesignation;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumSupporters(int numSupporters) {
        this.numSupporters = numSupporters;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setThemeDesignation(String themeDesignation) {
        this.themeDesignation = themeDesignation;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    @Override
    public String toString() {
        return title;
    }
}
