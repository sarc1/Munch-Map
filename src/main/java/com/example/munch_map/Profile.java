package com.example.munch_map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class Profile {

    public AnchorPane AnchorPaneProfile;
    public Label lblProfile;
    AnchorPane anchorPaneProfile;

    public void onEditProfileButtonClick() {
        try {
            AnchorPane p = AnchorPaneProfile;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MunchMap.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("munchmap.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
