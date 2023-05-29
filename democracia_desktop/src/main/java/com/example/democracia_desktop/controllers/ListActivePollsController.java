package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.navigateToScene;

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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ListActivePollsController {

  @FXML private ListView<PollModel> activePollsList;

  @FXML private Button backButton;

  @FXML private Button voteButton;

  @FXML
  void handleBackButton() {
    navigateToScene(this.getClass(), "/com/example/democracia_desktop/menu.fxml", backButton);
  }

  @FXML
  void handleVoteButton() {
    PollModel selectedItem = activePollsList.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      Long selectedItemId = activePollsList.getSelectionModel().getSelectedItem().getId();
      navigateToScene(
          selectedItemId,
          VoteController.class,
          "/com/example/democracia_desktop/vote.fxml",
          voteButton);
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
        .exceptionally(ControllerUtils::handleError);
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
}
