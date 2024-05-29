package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ViewPlaces {

    public AnchorPane viewPlacesAnchorPane, showAnchor;
    public ScrollPane showScroll;
    public ListView<String> placeListView;
    public Label barangayNameLabel;
    public ObservableList<String> places;

    public Label placeName;
    public Label placeRating;
    public Label placeType;
    public Button btnDetails;
    public Button btnMenu;
    public Button btnReviews;

    public Button btnBackPlaces;

    private String selectedPlaceName;


    public void initialize() {
        barangayNameLabel.setText(Barangay.selectedBarangay);
        places = FXCollections.observableArrayList();

        Task<ObservableList<String>> fetchPlacesTask = new FetchPlacesTask();
        placeListView.itemsProperty().bind(fetchPlacesTask.valueProperty());

        placeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPlaceName = newValue;
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
        return new Task<>() {
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
        return new Task<>() {
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

    public void backPlaceOnClick(ActionEvent actionEvent) {
        try {
            Parent barangayPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("barangay.fxml")));
            viewPlacesAnchorPane.getChildren().setAll(barangayPage);
            barangayPage.getStylesheets().clear();
            barangayPage.getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detailOnClick(ActionEvent actionEvent) {
            if (selectedPlaceName != null) {
                Task<PlaceDetails> fetchPlaceDetailsTask = new FetchPlaceDetailsTask(selectedPlaceName);
                fetchPlaceDetailsTask.setOnSucceeded(e -> displayPlaceDetails(fetchPlaceDetailsTask.getValue()));
                new Thread(fetchPlaceDetailsTask).start();
            }
    }

    public void menuOnClick(ActionEvent actionEvent) {
    }

    public void reviewsOnClick(ActionEvent actionEvent) {
        if (selectedPlaceName != null) {
            Task<ObservableList<Review>> fetchReviewsTask = new FetchReviewsTask(selectedPlaceName);
            fetchReviewsTask.setOnSucceeded(e -> displayReviews(fetchReviewsTask.getValue()));
            new Thread(fetchReviewsTask).start();
        }
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

    private static class FetchPlaceDetailsTask extends Task<PlaceDetails> {
        private final String placeName;

        FetchPlaceDetailsTask(String placeName) {
            this.placeName = placeName;
        }

        @Override
        protected PlaceDetails call() {
            try (Connection c = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = c.prepareStatement(
                         "SELECT p.place_name, p.place_type, p.place_address, p.place_landmark, p.place_about, AVG(r.rating) as avg_rating " +
                                 "FROM tblPlace p " +
                                 "LEFT JOIN tblReviews r ON p.place_id = r.place_id AND r.isApproved = 1 " +
                                 "WHERE p.place_name = ?;"
                 )) {

                statement.setString(1, placeName);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("place_name");
                    String avgRating = rs.getString("avg_rating");
                    String type = rs.getString("place_type");
                    String address = rs.getString("place_address");
                    String landmark = rs.getString("place_landmark");
                    String about = rs.getString("place_about");

                    return new PlaceDetails(name, avgRating != null ? avgRating : "N/A", type, address, landmark, about);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class FetchReviewsTask extends Task<ObservableList<Review>> {
        private final String placeName;

        FetchReviewsTask(String placeName) {
            this.placeName = placeName;
        }

        @Override
        protected ObservableList<Review> call() {
            ObservableList<Review> reviews = FXCollections.observableArrayList();
            try (Connection c = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = c.prepareStatement(
                         "SELECT r.review_id, r.rating, r.comment, a.username " +
                                 "FROM tblReviews r " +
                                 "INNER JOIN tblPlace p ON r.place_id = p.place_id " +
                                 "INNER JOIN tblAccount a ON r.acc_id = a.acc_id " +
                                 "WHERE p.place_name = ? AND r.isApproved = 1;"
                 )) {

                statement.setString(1, placeName);
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    double rating = rs.getDouble("rating");
                    String comment = rs.getString("comment");
                    String username = rs.getString("username");
                    reviews.add(new Review(rating, comment, username));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return reviews;
        }
    }

    private void displayReviews(ObservableList<Review> reviews) {
        VBox reviewsContainer = new VBox();
        for (Review review : reviews) {
            Label usernameLabel = new Label("User: " + review.getUsername());
            Label ratingLabel = new Label("Rating: " + review.getRating());
            Label commentLabel = new Label("Comment: " + review.getComment());
            reviewsContainer.getChildren().addAll(usernameLabel, ratingLabel, commentLabel);
        }
        showScroll.setContent(reviewsContainer);
    }

    private void displayPlaceDetails(PlaceDetails placeDetails) {
        VBox detailsContainer = new VBox();
        Label nameLabel = new Label("Name: " + placeDetails.getName());
        Label ratingLabel = new Label("Average Rating: " + placeDetails.getAvgRating());
        Label typeLabel = new Label("Type: " + placeDetails.getType());
        Label addressLabel = new Label("Address: " + placeDetails.getAddress());
        Label landmarkLabel = new Label("Landmark: " + placeDetails.getLandmark());
        Label aboutLabel = new Label("About: " + placeDetails.getAbout());

        detailsContainer.getChildren().addAll(nameLabel,ratingLabel, typeLabel, addressLabel, landmarkLabel, aboutLabel);
        showScroll.setContent(detailsContainer);
    }
}
