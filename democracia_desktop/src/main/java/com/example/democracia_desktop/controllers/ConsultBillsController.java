package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

import com.example.democracia_desktop.models.BillModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ConsultBillsController {

  @FXML private Button backButton;

  @FXML private Button billDetailsButton;

  @FXML private ListView<BillModel> openBillsList;

  @FXML
  void handleBackButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", backButton);
  }

  @FXML
  void handleBillDetailsButton() {
    BillModel selectedItem = openBillsList.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      Long selectedItemId = openBillsList.getSelectionModel().getSelectedItem().getId();
      navigateToScene(
          selectedItemId,
          ConsultBillDetailsController.class,
          "/com/example/democracia_desktop/consult_bill_details.fxml",
          billDetailsButton);
    }
  }

  public void initialize() {
    fetchAndDisplayConsultBills();
  }

  private void fetchAndDisplayConsultBills() {
    String apiUrl = "http://localhost:8080/api/bills/open";

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

    httpClient
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(this::updateOpenBillsList)
        .exceptionally(ControllerUtils::handleError);
  }

  private void updateOpenBillsList(String responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      List<BillModel> openBills = objectMapper.readValue(responseBody, new TypeReference<>() {});
      openBillsList.getItems().addAll(openBills);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
