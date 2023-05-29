package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AuthenticationController {

  @FXML private Button enterButton;

  @FXML
  void handleEnterButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", enterButton);
  }
}
