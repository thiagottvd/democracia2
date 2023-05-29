package com.example.democracia_desktop.controllers;

import static com.example.democracia_desktop.controllers.ControllerUtils.*;

import com.example.democracia_desktop.models.BillModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConsultBillDetailsController {

  @FXML private Label delegateLabel;

  @FXML private Label idLabel;

  @FXML private Button backButton;

  @FXML private Label descriptionLabel;

  @FXML private Label expirationDateLabel;

  @FXML private Label numSupportersLabel;

  @FXML private Label themeLabel;

  @FXML private Label titleLabel;

  private byte[] fileData;
  private Long selectedItemId;
  private static final String TYPE_SUCCESS = "Sucesso";
  private static final String TYPE_ERROR = "Erro";
  private static final String MESSAGE_SUCCESS = "Operação bem-sucedida.";

  @FXML
  void handleBackButton() {
    navigateToScene(
        this.getClass(), "/com/example/democracia_desktop/consult_bills.fxml", backButton);
  }

  @FXML
  void handleOpenFileButton() {
    File tempFile;
    try {
      tempFile = File.createTempFile(".pdf", "tempFile");
      try (FileOutputStream fos = new FileOutputStream(tempFile)) {
        fos.write(fileData);
      }
      openFile(tempFile);
      tempFile.deleteOnExit();
    } catch (IOException e) {
      throw new RuntimeException("Failed to create or write to temporary PDF file", e);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while opening the file", e);
    }
  }

  @FXML
  void handleSupportButton() {
    // Using a mock citizen card number to represent the session user
    int citizenCardNumberMock = 1;
    String apiUrl = "http://localhost:8080/api/bills/" + selectedItemId + "/support";
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .method(
                "PATCH", HttpRequest.BodyPublishers.ofString(String.valueOf(citizenCardNumberMock)))
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
              Platform.runLater(
                  () -> handleMessage(this.getClass(), TYPE_SUCCESS, MESSAGE_SUCCESS));
              fetchAndDisplayConsultBillDetails();
            })
        .exceptionally(
            ex -> {
              String errorMessage = updateMessage(ex.getMessage());
              Platform.runLater(() -> handleMessage(this.getClass(), TYPE_ERROR, errorMessage));
              return null;
            });
  }

  public void setSelectedItemId(Long selectedItemId) {
    this.selectedItemId = selectedItemId;
    initialize();
  }

  private void initialize() {
    fetchAndDisplayConsultBillDetails();
  }

  private void fetchAndDisplayConsultBillDetails() {
    String apiUrl = "http://localhost:8080/api/bills/" + selectedItemId;

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

    httpClient
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(this::updateBill)
        .exceptionally(
            ex -> {
              String errorMessage = ex.getMessage();
              if (!errorMessage.isEmpty()) {
                handleMessage(this.getClass(), TYPE_ERROR, errorMessage);
              }
              return null;
            });
  }

  private void updateBill(String responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    try {
      BillModel billModel = objectMapper.readValue(responseBody, new TypeReference<>() {});
      Platform.runLater(
          () -> {
            idLabel.setText(billModel.getId().toString());
            titleLabel.setText(billModel.getTitle());
            descriptionLabel.setText(billModel.getDescription());
            numSupportersLabel.setText(String.valueOf(billModel.getNumSupporters()));
            expirationDateLabel.setText(billModel.getExpirationDate().toString());
            themeLabel.setText(billModel.getThemeDesignation());
            delegateLabel.setText(billModel.getDelegateName());
            fileData = Arrays.copyOf(billModel.getFileData(), billModel.getFileData().length);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // https://stackoverflow.com/questions/53065887/opening-a-pdf-in-a-javafx-aplication
  public static void openFile(File file) {
    if (Desktop.isDesktopSupported()) {
      new Thread(
              () -> {
                try {
                  Desktop.getDesktop().open(file);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
    }
  }
}
