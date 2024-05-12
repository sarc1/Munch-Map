package com.example.munch_map;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MunchMap extends Application {

    @FXML
    AnchorPane LoginSignupPage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            TableInstantiation.initializeTables();
            Parent root = FXMLLoader.load(getClass().getResource("MunchMap.fxml"));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

//    private static void createDataTable() {
//        try (Connection c = MySQLConnection.getConnection();
//             Statement statement = c.createStatement()) {
//
//            String query = "CREATE TABLE IF NOT EXISTS tblData(" +
//                    "id INT AUTO_INCREMENT PRIMARY KEY," +
//                    "name VARCHAR(100) NOT NULL," +
//                    "location VARCHAR(100) NOT NULL," +
//                    "reviews INT NOT NULL" +
//                    ")";
//            statement.execute(query);
//            System.out.println("Data table created successfully");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

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
