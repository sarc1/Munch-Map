package com.example.munch_map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        TableInstantiation.initializeTables();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApp.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
//        AnchorPane root = new AnchorPane();
//        root.setId("pane");
        scene.getStylesheets().add(getClass().getResource("startview.css").toExternalForm());
//        stage.getIcons().add(new Image(StartApp.class.getResourceAsStream("resources/images/logo.png")));
        stage.setTitle("Munch Map");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}