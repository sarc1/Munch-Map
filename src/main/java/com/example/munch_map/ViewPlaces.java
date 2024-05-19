package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
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
    public ObservableList<String> places = FXCollections.observableArrayList();

    public Label placeName;
    public Label placeRating;
    public Label placeType;

    public void initialize() {
        updateBarangayLabel();
        setupListView();
    }

    private void updateBarangayLabel() {
        Service<Void> updateLabelService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        barangayNameLabel.setText(Barangay.selectedBarangay);
                        return null;
                    }
                };
            }
        };
        updateLabelService.start();
    }

    private void setupListView() {
        Service<Void> setupListViewService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        placeListView.itemsProperty().bind(new FetchPlacesTask().valueProperty());
                        placeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue != null) {
                                updatePlaceDetails(newValue);
                            }
                        });

                        new Thread(new FetchPlacesTask()).start();

                        return null;
                    }
                };
            }
        };
        setupListViewService.start();
    }

    private void updatePlaceDetails(String name) {
        Service<Void> updatePlaceDetailService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        placeName.setText(name);
                        placeRating.setText(retrieveDataFromDB("AVG(r.rating)", "avg_rating", name));
                        placeType.setText(retrieveDataFromDB("p.place_type", "place_type", name));
                        return null;
                    }
                };
            }
        };
        updatePlaceDetailService.start();
    }

    private String retrieveDataFromDB(String selectedData, String columnName, String placeName) {
        final String query = String.format("SELECT %s FROM tblReviews r INNER JOIN tblPlace p ON r.place_id = p.place_id WHERE p.place_name = ?", selectedData);
        try (Connection c = MySQLConnection.ds.getConnection();
             PreparedStatement statement = c.prepareStatement(query)) {
            statement.setString(1, placeName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString(columnName);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return "N/A";
    }

    class FetchPlacesTask extends Task<ObservableList<String>> {
        @Override
        protected ObservableList<String> call() {
            final String fetchPlacesQuery = "SELECT p.place_id, p.place_name FROM tblPlace p INNER JOIN tblBarangay b ON p.barangay_id = b.barangay_id WHERE b.barangay_name = ?";
            try (Connection c = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = c.prepareStatement(fetchPlacesQuery)) {
                statement.setString(1, Barangay.selectedBarangay);
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    places.add(rs.getString("place_name"));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return places;
        }
    }
}
