package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MunchMap {

    @FXML
    AnchorPane LoginSignupPage;

    public void onLoginButtonClick() throws IOException {
        AnchorPane p  = (AnchorPane) LoginSignupPage;
        Parent scene = FXMLLoader.load(getClass().getResource("Login.fxml"));
        p.getScene().getStylesheets().clear();
        p.getScene().getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    public void onSignUpButtonClick() throws IOException {
        AnchorPane p  = (AnchorPane) LoginSignupPage;
        Parent scene = FXMLLoader.load(getClass().getResource("Signup.fxml"));
        p.getScene().getStylesheets().clear();
        p.getScene().getStylesheets().add(getClass().getResource("signup.css").toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

}
