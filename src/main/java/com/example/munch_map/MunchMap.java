package com.example.munch_map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class MunchMap {

    @FXML
    AnchorPane LoginSignupPage;
    @FXML
    ProgressBar progressBar;

    public void onLoginButtonClick() throws IOException {
        AnchorPane p = LoginSignupPage;
        p.getScene().getStylesheets().clear();
        p.getChildren().clear();

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("transition.fxml")));
        Parent loading = fxmlLoader.load();
        Transition transitionController = fxmlLoader.getController();
        loading.getStylesheets().add(Objects.requireNonNull(getClass().getResource("transition.css")).toExternalForm());
        p.getChildren().add(loading);

        new Thread(() -> {
            try {
                Platform.runLater(() -> transitionController.setProgress(-1));
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
                Platform.runLater(() -> {
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
                    p.getChildren().clear();
                    p.getChildren().add(scene);

                    transitionController.setProgress(1);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> transitionController.setProgress(0));
            }
        }).start();
    }


    public void onSignUpButtonClick() throws IOException {
        AnchorPane p = LoginSignupPage;
        p.getScene().getStylesheets().clear();
        p.getChildren().clear();

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("transition.fxml")));
        Parent loading = fxmlLoader.load();
        loading.getStylesheets().add(Objects.requireNonNull(getClass().getResource("transition.css")).toExternalForm());
        Transition transitionController = fxmlLoader.getController();
        p.getChildren().add(loading);

        new Thread(() -> {
            try {
                Platform.runLater(() -> transitionController.setProgress(-1));
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup.fxml")));
                Platform.runLater(() -> {
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("signup.css")).toExternalForm());
                    p.getChildren().clear();
                    p.getChildren().add(scene);

                    transitionController.setProgress(1);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> transitionController.setProgress(0));
            }
        }).start();
    }
}