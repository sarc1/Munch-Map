package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Barangay {
    public AnchorPane barangayPage;
    public ComboBox<BarangayItem> barangayComboBox;
    public static String selectedBarangay;
    public Button btnAdmin;


    public void initialize() {
        Task<ObservableList<BarangayItem>> task = new Task<>() {
            @Override
            public ObservableList<BarangayItem> call() {

                ObservableList<BarangayItem> barangays = FXCollections.observableArrayList();

                try (Connection c = MySQLConnection.ds.getConnection();
                     Statement statement = c.createStatement()) {

                    String query = "SELECT * FROM tblBarangay;";
                    ResultSet list = statement.executeQuery(query);

                    while(list.next()) {
                        BarangayItem barangayItem = new BarangayItem(list.getString("barangay_name"));
                        barangays.add(barangayItem);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return barangays;
            }
        };

        task.setOnSucceeded(e -> barangayComboBox.setItems(task.getValue()));
        new Thread(task).start();

        // FIX: Improved implementation of Barangay Selection
        barangayComboBox.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    selectedBarangay = barangayComboBox.getSelectionModel().getSelectedItem().getName();
                    AnchorPane p = barangayPage;
                    Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view_places.fxml")));
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("view_places.css")).toExternalForm());
                    p.getChildren().clear();
                    p.getChildren().add(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handleViewAdmin(ActionEvent actionEvent) {
        try {
            if(Admin.isActive) {
                AnchorPane p = barangayPage;
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin_review.fxml")));
                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("admin_review.css")).toExternalForm());
                p.getChildren().clear();
                p.getChildren().add(scene);
                btnAdmin.setVisible(true);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

