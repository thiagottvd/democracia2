package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.*;

import com.example.democracia_desktop.datatypes.VoteType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class VoteController {

  @FXML private Button backButton;

  @FXML private Label delegateVoteLabel;

  @FXML private CheckBox hideVoteCheckBox;

  @FXML private RadioButton negativeVoteRadioButton;

  @FXML private RadioButton positiveVoteRadioButton;

  @FXML private ToggleGroup toggleGroup;

  private Long pollId;
  private static final String VOTE_UNAVAILABLE = "Voto indisponível.";
  private static boolean isVoteHidden = false;
  private static final String TYPE_SUCCESS = "Sucesso";
  private static final String TYPE_ERROR = "Erro";
  private static final String MESSAGE_SUCCESS = "Operação bem-sucedida.";
  private static final String MESSAGE_ERROR_SELECT_OPTION =
      "Você deve selecionar uma das opções de voto.";

  @FXML
  void handleBackButton() {
    navigateToScene(
        this.getClass(), "/com/example/democracia_desktop/active_polls_list.fxml", backButton);
  }

  @FXML
  void handleVoteButton() {
    RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
    if (selectedRadioButton != null) {
      // Using a mock citizen card number to represent the session user
      int citizenCardNumberMock = 1;
      VoteType voteType = null;
      if (selectedRadioButton == positiveVoteRadioButton) {
        voteType = VoteType.POSITIVE;
      } else if (selectedRadioButton == negativeVoteRadioButton) {
        voteType = VoteType.NEGATIVE;
      }
      String apiUrl = "http://localhost:8080/api/polls/" + pollId + "/vote/" + voteType;
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(apiUrl))
              .method(
                  "PATCH",
                  HttpRequest.BodyPublishers.ofString(String.valueOf(citizenCardNumberMock)))
              .header("Content-Type", "application/json")
              .build();
      httpClient
          .sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(
              response -> {
                int statusCode = response.statusCode();
                String responseBody = response.body();
                if (statusCode == 200) {
                  fetchCheckDelegateVote();
                  return responseBody;
                } else {
                  throw new RuntimeException(responseBody);
                }
              })
          .thenAccept(
              responseBody ->
                  Platform.runLater(
                      () -> handleMessage(this.getClass(), TYPE_SUCCESS, MESSAGE_SUCCESS)))
          .exceptionally(
              ex -> {
                String errorMessage = updateMessage(ex.getMessage());
                Platform.runLater(() -> handleMessage(this.getClass(), TYPE_ERROR, errorMessage));
                return null;
              });
    } else {
      handleMessage(this.getClass(), TYPE_ERROR, MESSAGE_ERROR_SELECT_OPTION);
    }
  }

  public void setSelectedItemId(Long selectedItemId) {
    this.pollId = selectedItemId;
    initialize();
  }

  private void initialize() {
    fetchCheckDelegateVote();

    hideVoteCheckBox.setSelected(isVoteHidden);
    delegateVoteLabel.setVisible(!isVoteHidden);

    hideVoteCheckBox
        .selectedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              delegateVoteLabel.setVisible(!newValue);
              isVoteHidden = newValue;
            });
  }

  private void fetchCheckDelegateVote() {
    // Using a mock citizen card number to represent the session user
    int citizenCardNumberMock = 1;
    String apiUrl = "http://localhost:8080/api/polls/" + pollId + "/delegate-vote-type";
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .method(
                "GET", HttpRequest.BodyPublishers.ofString(String.valueOf(citizenCardNumberMock)))
            .header("Content-Type", "application/json")
            .build();

    httpClient
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(this::updateCheckDelegateVote)
        .exceptionally(ControllerUtils::handleError);
  }

  private void updateCheckDelegateVote(String responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      if (!responseBody.isEmpty()) {
        String voteTypeStr = objectMapper.readValue(responseBody, new TypeReference<>() {});
        Platform.runLater(() -> delegateVoteLabel.setText(voteTypeStr));
      } else {
        Platform.runLater(() -> delegateVoteLabel.setText(VOTE_UNAVAILABLE));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
