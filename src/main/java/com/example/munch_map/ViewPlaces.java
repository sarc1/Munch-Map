package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ViewPlaces {

    public AnchorPane viewPlacesAnchorPane;
    public ListView<String> placeListView; // Replace with your place model class
    public Label barangayNameLabel;
    public ObservableList<String> places;

    public void initialize() {
        barangayNameLabel.setText(Barangay.selectedBarangay);
        places = FXCollections.observableArrayList();

        try (Connection c = MySQLConnection.ds.getConnection();
             PreparedStatement statement = c.prepareStatement(
                        "SELECT p.place_id, p.place_name " +
                             "FROM tblPlace p " +
                             "INNER JOIN tblBarangay b ON p.barangay_id = b.barangay_id " +
                             "WHERE b.barangay_name = ?;"
             )) {

            statement.setString(1, Barangay.selectedBarangay);
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                places.add(rs.getString("place_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        placeListView.setItems(places);
    }
}