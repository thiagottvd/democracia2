package com.example.democracia_desktop.controllers;

import com.example.democracia_desktop.models.BillModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class ConsultBillDetailsController {

    @FXML
    private Button backButton;

    @FXML
    private TextField delegateTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField expirationDateTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField numSupportersTextField;

    @FXML
    private TextField themeTextField;

    @FXML
    private TextField titleTextField;

    private byte[] fileData;

    @FXML
    void handleBackButton() {
        setupStage(backButton);
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

    private Long selectedItemId;

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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateBill)
                .exceptionally(this::handleError);
    }

    private void updateBill(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            BillModel billModel = objectMapper.readValue(responseBody, new TypeReference<>() {});
            idTextField.setText(billModel.getId().toString());
            titleTextField.setText(billModel.getTitle());
            descriptionTextArea.setText(billModel.getDescription());
            numSupportersTextField.setText(String.valueOf(billModel.getNumSupporters()));
            fileData = Arrays.copyOf(billModel.getFileData(), billModel.getFileData().length);
            expirationDateTextField.setText(billModel.getExpirationDate().toString());
            themeTextField.setText(billModel.getThemeDesignation());
            delegateTextField.setText(billModel.getDelegateName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Void handleError(Throwable throwable) {
        throwable.printStackTrace();
        return null;
    }

    private void setupStage(Button button) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/democracia_desktop/consult_bills.fxml"));
            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Democracia 2.0");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // https://stackoverflow.com/questions/53065887/opening-a-pdf-in-a-javafx-aplication
    public static void openFile(File file) {
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}

