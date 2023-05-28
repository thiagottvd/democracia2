package com.example.democracia_desktop.models;

public class PollModel {

  private Long id;

  private String title;

  public PollModel() {
    /*
    PollModel class requires a default public constructor for proper deserialization because Jackson,
    the JSON serialization/deserialization library, relies on either a default constructor or setter
    methods to instantiate objects during the deserialization process.
    */
  }

  public PollModel(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return title;
  }
}
