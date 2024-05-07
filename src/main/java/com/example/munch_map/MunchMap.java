package com.example.munch_map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MunchMap extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            createDataTable();
            Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDataTable() {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS tblData(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "location VARCHAR(100) NOT NULL," +
                    "reviews INT NOT NULL" +
                    ")";
            statement.execute(query);
            System.out.println("Data table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
