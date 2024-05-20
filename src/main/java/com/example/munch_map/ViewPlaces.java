package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        Task<ObservableList<String>> fetchPlacesTask = new FetchPlacesTask();
        placeListView.itemsProperty().bind(fetchPlacesTask.valueProperty());

        placeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updatePlaceDetails(newValue);
            }
        });

        new Thread(fetchPlacesTask).start();
    }

    private void updatePlaceDetails(String selectedPlaceName) {
        placeName.setText(selectedPlaceName);

        Task<String> getPlaceRatingFromDBTask = getPlaceRatingFromDBTask(selectedPlaceName);
        new Thread(getPlaceRatingFromDBTask).start();
        getPlaceRatingFromDBTask.setOnSucceeded(e -> placeRating.setText(getPlaceRatingFromDBTask.getValue()));

        Task<String> getPlaceTypeFromDBTask = getPlaceTypeFromDBTask(selectedPlaceName);
        new Thread(getPlaceTypeFromDBTask).start();
        getPlaceTypeFromDBTask.setOnSucceeded(e -> placeType.setText(getPlaceTypeFromDBTask.getValue()));
    }

    private Task<String> getPlaceRatingFromDBTask(String placeName) {
        return new Task<String>() {
            @Override
            public String call() {
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
                return "N/A";
            }
        };
    }

    private Task<String> getPlaceTypeFromDBTask(String placeName) {
        return new Task<String>() {
            @Override
            public String call() {
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
                return "N/A";
            }
        };
    }

    private class FetchPlacesTask extends Task<ObservableList<String>> {
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
