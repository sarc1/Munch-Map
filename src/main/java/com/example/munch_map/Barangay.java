package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Barangay {

    public AnchorPane barangayAnchorPane;
    public ComboBox<String> barangayComboBox;

    public void initialize() {
        Task<ObservableList<String>> task = new Task<>() {
            @Override
            public ObservableList<String> call() throws Exception {

                ObservableList<String> barangays = FXCollections.observableArrayList();

                try (Connection c = MySQLConnection.getConnection();
                     Statement statement = c.createStatement()) {

                    String query = "SELECT * FROM tblBarangay;";
                    ResultSet list = statement.executeQuery(query);

                    while(list.next()) {
                        String name = list.getString("barangay_name");
                        barangays.add(name);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return barangays;
            }
        };
        task.setOnSucceeded(e -> barangayComboBox.setItems(task.getValue()));
        new Thread(task).start();
    }
}
