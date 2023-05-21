package com.example.democracia_desktop.controllers;

import com.example.democracia_desktop.models.BillModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ConsultBillsController {

    @FXML
    private Button backButton;

    @FXML
    private Button billDetailsButton;

    @FXML
    private ListView<BillModel> openBillsList;

    @FXML
    void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/democracia_desktop/menu.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Democracia 2.0");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBillDetailsButton(ActionEvent event) {
        BillModel selectedItem = openBillsList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Long selectedItemId = openBillsList.getSelectionModel().getSelectedItem().getId();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/democracia_desktop/consult_bill_details.fxml"));

                Stage stage = (Stage) billDetailsButton.getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 800, 600);

                ConsultBillDetailsController controller = fxmlLoader.getController();
                controller.setSelectedItemId(selectedItemId);

                stage.setTitle("Democracia 2.0");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize() {
        fetchAndDisplayConsultBills();
    }

    private void fetchAndDisplayConsultBills() {
        String apiUrl = "http://localhost:8080/api/bills/open";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateOpenBillsList)
                .exceptionally(this::handleError);
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

    private Void handleError(Throwable throwable) {
        throwable.printStackTrace();
        return null;
    }

}
