package com.example.munch_map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class Editprofile {
    @FXML
    AnchorPane anchorPaneEdit;

    public void onEditProfileButtonClick() {
        try {
            AnchorPane p = anchorPaneEdit;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("profile.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            AnchorPane p = anchorPaneEdit;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("profile.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
