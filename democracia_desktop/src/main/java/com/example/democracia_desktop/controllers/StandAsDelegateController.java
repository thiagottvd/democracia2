package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.handleMessage;
import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StandAsDelegateController {

  @FXML private Button backButton;

  @FXML
  void handleBackButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", backButton);
  }

  @FXML
  void handleStandAsDelegateButton() {
    handleMessage(this.getClass(), "Mock Message", "This is a Mock message!");
  }
}
