package com.example.democracia_desktop.controllers;

import com.example.democracia_desktop.models.PollModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ListActivePollsController {

  @FXML private ListView<PollModel> activePollsList;

  @FXML private Button backButton;

  @FXML private Button voteButton;

  @FXML
  void handleBackButton() {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getResource("/com/example/democracia_desktop/menu.fxml"));
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
  void handleVoteButton() {
    PollModel selectedItem = activePollsList.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      Long selectedItemId = activePollsList.getSelectionModel().getSelectedItem().getId();
      try {
        FXMLLoader fxmlLoader =
            new FXMLLoader(getClass().getResource("/com/example/democracia_desktop/vote.fxml"));

        Stage stage = (Stage) voteButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        VoteController controller = fxmlLoader.getController();
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
    fetchAndDisplayActivePolls();
  }

  private void fetchAndDisplayActivePolls() {
    String apiUrl = "http://localhost:8080/api/polls/active";

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

    httpClient
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(this::updateActivePollsList)
        .exceptionally(this::handleError);
  }

  private void updateActivePollsList(String responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      List<PollModel> activePolls = objectMapper.readValue(responseBody, new TypeReference<>() {});
      activePollsList.getItems().addAll(activePolls);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Void handleError(Throwable throwable) {
    throwable.printStackTrace();
    return null;
  }
}
