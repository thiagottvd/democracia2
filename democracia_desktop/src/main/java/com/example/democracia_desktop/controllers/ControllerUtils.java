package com.example.democracia_desktop.controllers;

import com.example.democracia_desktop.models.MessageModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerUtils {

  static void navigateToScene(Class<?> klass, String resource, Button button) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(klass.getResource(resource));
      Stage stage = (Stage) button.getScene().getWindow();
      Scene scene = new Scene(fxmlLoader.load(), 800, 600);
      stage.setTitle("Democracia 2.0");
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void navigateToScene(Long selectedItemId, Class<?> klass, String resource, Button button) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(klass.getResource(resource));

      Stage stage = (Stage) button.getScene().getWindow();
      Scene scene = new Scene(fxmlLoader.load(), 800, 600);

      Object controller = fxmlLoader.getController();
      Method setSelectedItemIdMethod =
          controller.getClass().getMethod("setSelectedItemId", Long.class);
      setSelectedItemIdMethod.invoke(controller, selectedItemId);

      stage.setTitle("Democracia 2.0");
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  static String updateMessage(String responseBody) {
    String extractedMessage = extractMessage(responseBody);
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

  private static String extractMessage(String responseBody) {
    int startIndex = responseBody.indexOf('{');
    int endIndex = responseBody.lastIndexOf('}');
    String extractedMessage = null;

    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
      extractedMessage = responseBody.substring(startIndex, endIndex + 1);
    }
    return extractedMessage;
  }

  static void handleMessage(Class<?> klass, String type, String message) {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(klass.getResource("/com/example/democracia_desktop/message.fxml"));
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

  static Void handleError(Throwable throwable) {
    throwable.printStackTrace();
    return null;
  }
}
