package com.example.democracia_desktop.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

  @FXML private Button consultBillsButton;

  @FXML private Button listActivePollsButton;

  @FXML
  void handleListActivePollsButton() {
    setupStage("/com/example/democracia_desktop/active_polls_list.fxml", listActivePollsButton);
  }

  @FXML
  void handleConsultBillsButton() {
    setupStage("/com/example/democracia_desktop/consult_bills.fxml", consultBillsButton);
  }

  private void setupStage(String resource, Button button) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
      Stage stage = (Stage) button.getScene().getWindow();
      Scene scene = new Scene(fxmlLoader.load(), 800, 600);
      stage.setTitle("Democracia 2.0");
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}