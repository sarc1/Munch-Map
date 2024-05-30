package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    @FXML
    public WebView webView;

    private WebEngine webEngine;


    public void initialize() {
        showScroll.setVisible(false);
        barangayNameLabel.setText(Barangay.selectedBarangay);
        places = FXCollections.observableArrayList();

        webEngine = webView.getEngine();

        Task<ObservableList<String>> fetchPlacesTask = new FetchPlacesTask();
        placeListView.itemsProperty().bind(fetchPlacesTask.valueProperty());

        placeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPlaceName = newValue;
                updatePlaceDetails(newValue);
                loadWebPage(newValue);
            }
        });

        new Thread(fetchPlacesTask).start();
    }
    private void loadWebPage(String placeName) {
        String url = getUrlForPlace(placeName);
        webEngine.load(url);
    }
    private String getUrlForPlace(String placeName) {
        if (placeName.equalsIgnoreCase("Labangon")) {
            return "https://www.google.com/maps/place/Labangon,+Cebu+City,+6000+Cebu/@10.3002573,123.8743257,16z/data=!3m1!4b1!4m6!3m5!1s0x33a99ea9f188d865:0x31ab47369f9dc75b!8m2!3d10.2989677!4d123.8813181!16s%2Fg%2F1vntgfdf?entry=ttu";
        } else if (placeName.equalsIgnoreCase("Tisa")) {
            return "https://www.google.com/maps/place/Barangay+Tisa,+Cebu+City,+6000+Cebu/@10.3055611,123.8472244,15z/data=!3m1!4b1!4m6!3m5!1s0x33a99ea146df6987:0x8da9f7a3a4249dc9!8m2!3d10.301553!4d123.870527!16s%2Fg%2F1tnspg8g?entry=ttu";
        } else if (placeName.equalsIgnoreCase("Guadalupe")) {
            return "https://www.google.com/maps/place/Guadalupe,+Cebu+City,+6000+Cebu/@10.3213785,123.8628325,15z/data=!3m1!4b1!4m6!3m5!1s0x33a99ecedffd4d93:0x6c00a0cf0efb2913!8m2!3d10.3156173!4d123.8854377!16s%2Fg%2F1thxyb4r?entry=ttu";
        } else if (placeName.equalsIgnoreCase("Duljo")) {
            return "https://www.google.com/maps/place/Duljo,+Cebu+City,+6000+Cebu/@10.2925942,123.8813515,17z/data=!3m1!4b1!4m6!3m5!1s0x33a99c07177c46b5:0x3989caca21d80758!8m2!3d10.292778!4d123.8843799!16s%2Fg%2F1t_wlwz8?entry=ttu";
        } else if (placeName.equalsIgnoreCase("Mambaling")) {
            return "https://www.google.com/maps/place/Mambaling,+Cebu+City,+6000+Cebu/@10.2899455,123.8755209,16z/data=!3m1!4b1!4m6!3m5!1s0x33a99c060465097d:0xab08b428c97c343d!8m2!3d10.288614!4d123.8788002!16s%2Fg%2F11gbfb5c88?entry=ttu";
        } else if (placeName.equalsIgnoreCase("Punta Princesa")) {
            return "https://www.google.com/maps/place/Punta+Princesa,+Cebu+City,+6000+Cebu/@10.298341,123.8632225,16z/data=!3m1!4b1!4m6!3m5!1s0x33a99c1f2d85f5fb:0xc0d965f44d5114d1!8m2!3d10.294754!4d123.8676399!16s%2Fg%2F11gzkhr05?entry=ttu";
        } else if (placeName.equalsIgnoreCase("Capitol Site")) {
            return "https://www.google.com/maps/place/Capitol+Site,+Cebu+City,+6000+Cebu/@10.3147989,123.8859058,16z/data=!3m1!4b1!4m6!3m5!1s0x33a9994bd5cc124d:0x9ea79f31faf2414a!8m2!3d10.3143431!4d123.8906566!16s%2Fg%2F1tfxvk4f?entry=ttu";
        } else {
            return "https://www.google.com/maps/place/Cebu+City,+6000+Cebu/@10.3787539,123.7637215,12z/data=!3m1!4b1!4m6!3m5!1s0x33a999258dcd2dfd:0x4c34030cdbd33507!8m2!3d10.3156992!4d123.8854366!16zL20vMDFwX2x5?entry=ttu"; // Default URL for unknown places
        }
    }

    private void updatePlaceDetails(String selectedPlaceName) {
        placeName.setText(selectedPlaceName);

        showScroll.setVisible(false);

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
                             "SELECT ROUND(AVG(r.rating), 2) as avg_rating " +
                                     "FROM tblReviews r " +
                                     "INNER JOIN tblPlace p ON r.place_id = p.place_id AND r.isApproved = 1 " +
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
            viewPlacesAnchorPane.getScene().getStylesheets().clear();
            viewPlacesAnchorPane.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
            viewPlacesAnchorPane.getChildren().add(barangayPage);
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
                         "SELECT p.place_name, p.place_type, p.place_address, p.place_landmark, p.place_about, ROUND(AVG(r.rating), 2) as avg_rating " +
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

    private void displayPlaceDetails(PlaceDetails placeDetails) {
        showScroll.setVisible(true);
        VBox box = new VBox(10);
        box.setPadding(new Insets(0, 10, 0, 10));

        box.getChildren().addAll(
                new HBox(new Label("Name: "), new Label(placeDetails.getName())),
                new HBox(new Label("Average Rating: "), new Label(placeDetails.getAvgRating())),
                new HBox(new Label("Type: "), new Label(placeDetails.getType())),
                new HBox(new Label("Address: "), new Label(placeDetails.getAddress())),
                new HBox(new Label("Landmark: "), new Label(placeDetails.getLandmark())),
                new HBox(new Label("About: "), new Label(placeDetails.getAbout()))
        );
        showScroll.setContent(box);
    }

    private void displayReviews(ObservableList<Review> reviews) {
        showScroll.setVisible(true);
        VBox reviewsContainer = new VBox();

        for (Review review : reviews) {
            VBox singleReviewBox = new VBox();
            singleReviewBox.getChildren().addAll(
                    new HBox(new Label("User: "), new Label(review.getUsername())),
                    new HBox(new Label("Rating: "), new Label(String.valueOf(review.getRating()))),
                    new HBox(new Label("Comment: "), new Label(review.getComment()))
            );
            singleReviewBox.getStyleClass().add("review-box");
            reviewsContainer.getChildren().add(singleReviewBox);
        }
        reviewsContainer.prefWidthProperty().bind(showScroll.widthProperty());
        showScroll.setContent(reviewsContainer);
    }
}
