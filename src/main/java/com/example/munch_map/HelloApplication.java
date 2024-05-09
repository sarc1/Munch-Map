package com.example.munch_map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
//        AnchorPane root = new AnchorPane();
//        root.setId("pane");
        scene.getStylesheets().add(getClass().getResource("helloview.css").toExternalForm());
//        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("resources/images/logo.png")));
        stage.setTitle("Munch Map");
        stage.setScene(scene);
        stage. setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}