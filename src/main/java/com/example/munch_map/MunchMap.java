package com.example.munch_map;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class MunchMap {

    @FXML
    AnchorPane LoginSignupPage;

    public void onLoginButtonClick() throws IOException {
        AnchorPane p = LoginSignupPage;
        p.getScene().getStylesheets().clear();
        p.getChildren().clear();

        Parent loading = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("transition.fxml")));
        loading.getStylesheets().add(Objects.requireNonNull(getClass().getResource("transition.css")).toExternalForm());
        p.getChildren().add(loading);

        new Thread(() -> {
            try {
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));

                Platform.runLater(() -> {
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
                    p.getChildren().clear();
                    p.getChildren().add(scene);

                    FadeTransition ft = new FadeTransition(Duration.millis(500), scene);
                    ft.setFromValue(0.0);
                    ft.setToValue(1.0);
                    ft.play();
                });
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> p.getChildren().remove(loading));
            }
        }).start();
    }

    public void onSignUpButtonClick() throws IOException {
        AnchorPane p = LoginSignupPage;
        p.getScene().getStylesheets().clear();
        p.getChildren().clear();

        Parent loading = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("transition.fxml")));
        loading.getStylesheets().add(Objects.requireNonNull(getClass().getResource("transition.css")).toExternalForm());
        p.getChildren().add(loading);

        new Thread(() -> {
            try {
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup.fxml")));

                Platform.runLater(() -> {
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("signup.css")).toExternalForm());
                    p.getChildren().clear();
                    p.getChildren().add(scene);

                    FadeTransition ft = new FadeTransition(Duration.millis(500), scene);
                    ft.setFromValue(0.0);
                    ft.setToValue(1.0);
                    ft.play();
                });
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> p.getChildren().remove(loading));
            }
        }).start();
    }

}
