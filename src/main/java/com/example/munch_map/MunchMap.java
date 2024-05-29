package com.example.munch_map;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class MunchMap {

    @FXML
    AnchorPane LoginSignupPage;
    @FXML
    Text lblMunchMap;
    @FXML
    Button btnLogin;
    @FXML
    Button btnSignup;

    @FXML
    public void initialize() {
        lblMunchMap.setOpacity(0.0);
        btnSignup.setOpacity(0.0);
        btnLogin.setOpacity(0.0);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2500), lblMunchMap);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(2500), btnSignup);
        fadeTransition1.setFromValue(0.0);
        fadeTransition1.setToValue(1.0);
        FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(2500), btnLogin);
        fadeTransition2.setFromValue(0.0);
        fadeTransition2.setToValue(1.0);
        fadeTransition.play();
        fadeTransition1.play();
        fadeTransition2.play();
    }
    @FXML
    public void onLoginButtonClick() throws IOException {
        AnchorPane p = LoginSignupPage;
        p.getScene().getStylesheets().clear();
        p.getChildren().clear();

        Parent loading = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("transition.fxml")));
        loading.getStylesheets().add(Objects.requireNonNull(getClass().getResource("transition.css")).toExternalForm());
        p.getChildren().add(loading);

//        Image image = new Image();
//        ImageView imageView = new ImageView(image);
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(imageView);
//        p.getChildren().add(stackPane);



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
