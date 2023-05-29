package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ListExpiredPollsController {

  @FXML private Button backButton;

  @FXML private ListView<String> expiredPollsList;

  @FXML
  void handleBackButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", backButton);
  }

  @FXML
  public void initialize() {
    // Mock data
    List<String> expiredPolls =
        new ArrayList<>(
            Arrays.asList(
                "Aumentar Propina das Universidades Públicas",
                "Diminuir Imposto",
                "Construir Novas Escolas",
                "Reformar Hospital Santa Maria"));
    expiredPollsList.getItems().addAll(expiredPolls);
  }
}
