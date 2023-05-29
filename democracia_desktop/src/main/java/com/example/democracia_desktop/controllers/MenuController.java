package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

  @FXML private Button consultBillsButton;

  @FXML private Button listActivePollsButton;

  @FXML private Button standAsDelegateButton;

  @FXML private Button chooseDelegateButton;

  @FXML private Button listExpiredPollsButton;

  @FXML private Button proposeBillButton;

  @FXML
  void handleStandAsDelegateButton() {
    // TODO
  }

  @FXML
  void handleListExpiredPollsButton() {
    // TODO
  }

  @FXML
  void handleListActivePollsButton() {
    navigateToScene(
        this.getClass(),
        "/com/example/democracia_desktop/active_polls_list.fxml",
        listActivePollsButton);
  }

  @FXML
  void handleProposeBillButton() {
    // TODO
  }

  @FXML
  void handleConsultBillsButton() {
    navigateToScene(
        this.getClass(), "/com/example/democracia_desktop/consult_bills.fxml", consultBillsButton);
  }

  @FXML
  void handleChooseDelegateButton() {
    navigateToScene(
        this.getClass(),
        "/com/example/democracia_desktop/choose_delegate_mock.fxml",
        chooseDelegateButton);
  }
}
