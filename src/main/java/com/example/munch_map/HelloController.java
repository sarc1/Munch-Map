package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    AnchorPane helloviewpn;

    @FXML
    protected void onEnterButtonClick() throws IOException {
        AnchorPane p  = (AnchorPane) helloviewpn;
        Parent scene = FXMLLoader.load(getClass().getResource("MunchMap.fxml"));
        p.getScene().getStylesheets().clear();
        p.getScene().getStylesheets().add(getClass().getResource("munchmap.css").toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}