package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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

public class Barangay {

    public AnchorPane barangayPage;
    public ComboBox<String> barangayComboBox;
    public Button btnBarangay;
    private ViewPlaces viewPlaces;
    public static String selectedBarangay;

    public void initialize() {
        Task<ObservableList<String>> task = new Task<>() {
            @Override
            public ObservableList<String> call() throws Exception {

                ObservableList<String> barangays = FXCollections.observableArrayList();

                try (Connection c = MySQLConnection.ds.getConnection();
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

    public void goToBarangay(ActionEvent actionEvent) throws IOException {
        selectedBarangay = barangayComboBox.getSelectionModel().getSelectedItem();
        AnchorPane p = barangayPage;
        // TOD0: Link to MunchMap Main Page and CSS
        Parent scene = FXMLLoader.load(getClass().getResource("view_places.fxml"));
        p.getScene().getStylesheets().clear();
//        p.getScene().getStylesheets().add(getClass().getResource("munchmap.css").toExternalForm());
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}
