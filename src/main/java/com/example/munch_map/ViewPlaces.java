package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class ViewPlaces {

    public AnchorPane viewPlacesAnchorPane;
    public ListView<String> placeListView;
    public Label barangayNameLabel;
    public ObservableList<String> places;

    public Label placeName;
    public Label placeRating;
    public Label placeType;

    public void initialize() {
        barangayNameLabel.setText(Barangay.selectedBarangay);
        places = FXCollections.observableArrayList();

        Task<ObservableList<String>> task = new FetchPlacesTask();
        placeListView.itemsProperty().bind(task.valueProperty());

        placeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' contains the selected place name. You can use 'newValue' to fetch ratings from your database.
            String selectedPlaceName = newValue;

            if (selectedPlaceName != null) {
                updatePlaceDetails(selectedPlaceName);
            }
        });

        new Thread(task).start();
    }

    private void updatePlaceDetails(String selectedPlaceName) {
        // Use selectedPlaceName to fetch data from your database and update 'placeName' and 'placeRatings' labels.

        // Assume 'getPlaceNameFromDB' and 'getPlaceRatingsFromDB' are your methods to fetch data from the database.
        placeName.setText(selectedPlaceName); // Set the new placeName to the Label.
        placeRating.setText(getPlaceRatingFromDB(selectedPlaceName)); // Set the new placeRating to the Label.
        placeType.setText(getPlaceTypeFromDB(selectedPlaceName));
    }

    private String getPlaceRatingFromDB(String placeName) {
        // This function should go to the DB and retrieve the average rating for a provided placeName.

        try (Connection c = MySQLConnection.ds.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT AVG(r.rating) as avg_rating " +
                             "FROM tblReviews r " +
                             "INNER JOIN tblPlace p ON r.place_id = p.place_id " +
                             "WHERE p.place_name = ?;"
             )) {

            statement.setString(1, placeName);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                return rs.getString("avg_rating");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "N/A"; // Return "Not Available" if place has no rating yet.
    }

    private String getPlaceTypeFromDB(String placeName) {
        // This function should go to the DB and retrieve the place type for a provided placeName.

        try (Connection c = MySQLConnection.ds.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT p.place_type " +
                             "FROM tblPlace p " +
                             "WHERE p.place_name = ?;"
             )) {

            statement.setString(1, placeName);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                return rs.getString("place_type");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "N/A"; // Return "Not Available" if place type is not found.
    }

    class FetchPlacesTask extends Task<ObservableList<String>> {
        @Override
        protected ObservableList<String> call() {
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

            return places;
        }


    }
}