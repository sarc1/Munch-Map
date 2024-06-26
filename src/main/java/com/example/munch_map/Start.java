package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class Start {
    @FXML
    private Label welcomeText;
    @FXML
    AnchorPane helloviewpn;

    @FXML
    protected void onEnterButtonClick() throws IOException {
        TableInstantiation.initializeTables();
        AnchorPane p  = helloviewpn;
        Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MunchMap.fxml")));
        p.getScene().getStylesheets().clear();
        p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("munchmap.css")).toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}