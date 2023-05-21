package com.example.democracia_desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.util.Base64;

public class PdfController {

    @FXML
    private WebView pdfWebView;

    private byte[] fileData;

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
        render();
    }

    private void render() {
        String base64Data = Base64.getEncoder().encodeToString(fileData);

        pdfWebView.getEngine().loadContent("<embed src=\"data:application/pdf;base64," + base64Data + "\" width=\"100%\" height=\"100%\"/>");
    }
}
