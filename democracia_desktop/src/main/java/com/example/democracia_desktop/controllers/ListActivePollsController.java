package com.example.democracia_desktop.controllers;


import com.example.democracia_desktop.models.PollModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class ListActivePollsController {

    @FXML
    private ListView<String> activePollsList;

    @FXML
    private Button backButton;

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

    public void initialize() {
        fetchAndDisplayActivePolls();
    }

    private void fetchAndDisplayActivePolls() {
        String apiUrl = "http://localhost:8080/api/polls/active";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateActivePollsList)
                .exceptionally(this::handleError);
    }

    private void updateActivePollsList(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<PollModel> activePolls = objectMapper.readValue(responseBody, new TypeReference<List<PollModel>>() {});
            List<String> pollTitles = activePolls.stream()
                    .map(PollModel::getTitle)
                    .collect(Collectors.toList());
            activePollsList.getItems().addAll(pollTitles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Void handleError(Throwable throwable) {
        throwable.printStackTrace();
        return null;
    }

}
