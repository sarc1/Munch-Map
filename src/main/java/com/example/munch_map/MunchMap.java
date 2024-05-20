package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Objects;

public class MunchMap {

    @FXML
    AnchorPane LoginSignupPage;

    public void onLoginButtonClick() throws IOException {
        AnchorPane p  = LoginSignupPage;
        Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        p.getScene().getStylesheets().clear();
        p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    public void onSignUpButtonClick() throws IOException {
        AnchorPane p  = LoginSignupPage;
        Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup.fxml")));
        p.getScene().getStylesheets().clear();
        p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("signup.css")).toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

}
