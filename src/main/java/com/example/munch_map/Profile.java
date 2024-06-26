package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class Profile {

    public AnchorPane AnchorPaneProfile;
    public Label lblProfile, displayUsername, displayEmail;

    @FXML
    public void initialize() {
        if (Login.activeUser != null) {
            displayUsername.setText(Login.activeUser.getUsername());
            displayEmail.setText(Login.activeUser.getEmail());
        } else {
            displayUsername.setText(Login.activeAdmin.getUsername());
            displayEmail.setText(Login.activeAdmin.getEmail());
        }
    }
    public void onEditProfileButtonClick() {
        try {
            AnchorPane p = AnchorPaneProfile;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("editprofile.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("editprofile.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onReturnButtonClick() {
        try {
            AnchorPane p = AnchorPaneProfile;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("barangay.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onLogoutButtonClick() {
        try {
            if(Login.activeAdmin.isActive()) {
                Login.activeAdmin = null;
            } else {
                Login.activeUser = null;
            }
            AnchorPane p = AnchorPaneProfile;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
