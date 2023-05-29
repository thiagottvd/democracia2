package com.example.democracia_desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class MessageController {

  @FXML private Text messageText;

  @FXML private Label typeLabel;

  public void setType(String type) {
    typeLabel.setText(type);
  }

  public void setMessage(String message) {
    messageText.setText(message);
  }
}
