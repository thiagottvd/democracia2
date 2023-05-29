package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.handleMessage;
import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class ChooseDelegateController {

  @FXML private Button backButton;

  @FXML private Button chooseDelegateButton;

  @FXML private ChoiceBox<String> delegateChoiceBox;

  @FXML private ChoiceBox<String> themeChoiceBox;

  @FXML
  void handleBackButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", backButton);
  }

  @FXML
  void handleChooseDelegateButton() {
    handleMessage(this.getClass(), "Mock Message", "This is a Mock message!");
  }

  @FXML
  public void initialize() {
    // Mock data
    List<String> delegates =
        new ArrayList<>(Arrays.asList("Jose P.", "Isabel R.", "Eloi W.", "Ema S.", "Paulo S."));
    ObservableList<String> observableDelegates = FXCollections.observableList(delegates);
    delegateChoiceBox.setItems(observableDelegates);

    // Mock data
    List<String> themes =
        new ArrayList<>(
            Arrays.asList(
                "Saude",
                "Hospital",
                "Profissional Saude",
                "Equipamentos Hospitalares",
                "Economia",
                "Imposto"));
    ObservableList<String> observableThemes = FXCollections.observableList(themes);
    themeChoiceBox.setItems(observableThemes);
  }
}
