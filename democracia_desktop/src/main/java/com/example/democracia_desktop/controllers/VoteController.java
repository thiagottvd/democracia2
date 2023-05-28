package com.example.democracia_desktop.controllers;

import com.example.democracia_desktop.datatypes.VoteType;
import com.example.democracia_desktop.models.MessageModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(
              getClass().getResource("/com/example/democracia_desktop/active_polls_list.fxml"));
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
  void handleVoteButton(ActionEvent event) {
    RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
    if (selectedRadioButton != null) {
      // Using a mock citizen card number to represent the session user
      int citizenCardNumberMock = 1;
      VoteType voteType;
      if (selectedRadioButton == positiveVoteRadioButton) {
        voteType = VoteType.POSITIVE;
      } else {
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
                  return responseBody;
                } else {
                  throw new RuntimeException(responseBody);
                }
              })
          .thenAccept(
              responseBody -> {
                Platform.runLater(() -> handleMessage(TYPE_SUCCESS, MESSAGE_SUCCESS));
              })
          .exceptionally(
              ex -> {
                String errorMessage = updateMessage(ex.getMessage());
                Platform.runLater(() -> handleMessage(TYPE_ERROR, errorMessage));
                return null;
              });
    } else {
      handleMessage(TYPE_ERROR, MESSAGE_ERROR_SELECT_OPTION);
    }
  }

  public void setSelectedItemId(Long selectedItemId) {
    this.pollId = selectedItemId;
    initialize();
  }

  private void initialize() {
    fetchAndDisplayCheckDelegateVote();

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

  private void fetchAndDisplayCheckDelegateVote() {
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
        .exceptionally(this::handleError);
  }

  private void updateCheckDelegateVote(String responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      if (!responseBody.isEmpty()) {
        String voteTypeStr = objectMapper.readValue(responseBody, new TypeReference<>() {});
        Platform.runLater(
            () -> {
              delegateVoteLabel.setText(voteTypeStr);
            });
      } else {
        Platform.runLater(
            () -> {
              delegateVoteLabel.setText(VOTE_UNAVAILABLE);
            });
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleMessage(String type, String message) {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getResource("/com/example/democracia_desktop/message.fxml"));
      Stage newStage = new Stage();
      Scene newScene = new Scene(fxmlLoader.load(), 450, 250);
      MessageController controller = fxmlLoader.getController();
      controller.setType(type);
      controller.setMessage(message);
      newStage.setScene(newScene);
      newStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String updateMessage(String responseBody) {
    int startIndex = responseBody.indexOf('{');
    int endIndex = responseBody.lastIndexOf('}');
    String extractedMessage = null;

    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
      extractedMessage = responseBody.substring(startIndex, endIndex + 1);
    }
    MessageModel messageModel;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    try {
      messageModel = objectMapper.readValue(extractedMessage, new TypeReference<>() {});
      if (messageModel != null) {
        return messageModel.getMessage();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "An error occurred while processing the response.";
  }

  private Void handleError(Throwable throwable) {
    throwable.printStackTrace();
    return null;
  }
}
