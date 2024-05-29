package com.example.munch_map;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AddPlace {

    @FXML
    private AnchorPane AddPlace;

    @FXML
    private TextField barangayInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField typeInput;

    @FXML
    private TextField addressInput;

    @FXML
    private Button btnAdd;


    @FXML
    private void onAddButtonClick() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (Connection c = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO tblPlace (barangay_id, place_name, place_type, place_address) VALUES (?, ?, ?, ?)"
                     )) {

                    PreparedStatement checker = c.prepareStatement("SELECT * FROM tblPlace WHERE place_name = ?");

                    checker.setString(1, nameInput.getText());

                    ResultSet list = checker.executeQuery();

                    if(list.next()) {
                        // TOD0: Implement Error Message
                        System.out.println("Restaurant already exists!");
                    } else {
                        statement.setString(1, barangayInput.getText());
                        statement.setString(2, nameInput.getText());
                        statement.setString(3, typeInput.getText());
                        statement.setString(4, addressInput.getText());
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            try {
                AnchorPane p = AddPlace;
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view_places.fxml")));
                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("view_places.css")).toExternalForm());
                p.getChildren().clear();
                p.getChildren().add(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            AnchorPane p = AddPlace;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("barangay.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
