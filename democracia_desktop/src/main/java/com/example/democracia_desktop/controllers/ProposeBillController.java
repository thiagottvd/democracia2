package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.handleMessage;
import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProposeBillController {

  @FXML private Button backButton;

  @FXML private Label fileNameLabel;

  @FXML private TextArea descriptionTextArea;

  @FXML private DatePicker expirationDatePicker;

  @FXML private Button loadFileButton;

  @FXML private ChoiceBox<String> themesChoiceBox;

  @FXML private TextField titleTextField;

  @FXML
  void handleBackButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", backButton);
  }

  @FXML
  void handleOpenFileButton() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

    Stage stage = (Stage) loadFileButton.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      fileNameLabel.setText(selectedFile.getName());
    }
  }

  @FXML
  void handleProposeBillButton() {
    handleMessage(this.getClass(), "Mock Message", "This is a Mock message!");
  }

  @FXML
  public void initialize() {
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
    themesChoiceBox.setItems(observableThemes);
  }
}
