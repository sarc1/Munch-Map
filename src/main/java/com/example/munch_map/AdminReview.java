package com.example.munch_map;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AdminReview {
    @FXML
    private ChoiceBox<String> cbAdminReview;

    @FXML
    private TreeView<String> treeviewReview;

    @FXML
    private TextField txtComment;

    @FXML
    private Label lblReviewID, lblPlace, lblUsername, lblRating;

    @FXML
    private AnchorPane anchorpaneAdminReview;

    @FXML
    private List<Review> reviews = new ArrayList<>();
    private int currentReviewIndex = -1;

    private List<Places> places = new ArrayList<>();
    private int currentPlaceIndex = -1;

    private String chosenReviewStatus = "";

    @FXML
    Button btnDeny, btnApprove, btnNext, btnPrev;


    @FXML
    public void initialize() {
        // Initialize the ComboBox or other components if needed\

        cbAdminReview.getItems().addAll("Reviews (Unchecked)","Reviews (Approved)","Reviews (Deleted)","New Places (Unchecked)","New Places (Approved)","New Places (Deleted)");

        treeviewReview.setShowRoot(false);
        cbAdminReview.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null && newValue.startsWith("Reviews")) {
                chosenReviewStatus = newValue.replace("Reviews (", "").replace(")", "");

                Task<TreeItem<String>> fetchReviewsTask = new FetchReviewsTask();
                treeviewReview.rootProperty().bind(fetchReviewsTask.valueProperty());
                 // Makes the root node invisible
                if (!newValue.equals(oldValue)) {
                    reviews.clear();
                    currentReviewIndex = 0;
                }

                new Thread(fetchReviewsTask).start();
                updateReviewDetails();

            }else if (newValue.equals("New Places (Unchecked)")) {
                chosenReviewStatus = "New Places (Unchecked)";
                Task<TreeItem<String>> fetchPlacesTask = new FetchPlacesTask(false, false);
                treeviewReview.rootProperty().bind(fetchPlacesTask.valueProperty());
                new Thread(fetchPlacesTask).start();
                hideButtons(true);
                updatePlaceDetails();
            } else if (newValue.equals("New Places (Approved)")) {
                chosenReviewStatus = "New Places (Approved)";
                Task<TreeItem<String>> fetchPlacesTask = new FetchPlacesTask(true, false);
                treeviewReview.rootProperty().bind(fetchPlacesTask.valueProperty());
                new Thread(fetchPlacesTask).start();
                updatePlaceDetails();
            }else if (newValue.equals("New Places (Deleted)")) {
                chosenReviewStatus = "New Places (Deleted)";
                Task<TreeItem<String>> fetchPlacesTask = new FetchPlacesTask(false, true); // pass true for fetchDeleted
                treeviewReview.rootProperty().bind(fetchPlacesTask.valueProperty());
                new Thread(fetchPlacesTask).start();
                updatePlaceDetails();
            }

        });

        treeviewReview.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedItem = newValue.getValue();
                if (selectedItem.startsWith("Review#")) {
                    loadReviewDetails(selectedItem);
                } else if (selectedItem.startsWith("Place#")) {
                    loadPlaceDetails(selectedItem);
                }
            }
        });
    }

    // Method to recursively expand all nodes in the TreeView
    private void expandTreeView(TreeItem<String> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<String> child : item.getChildren()) {
                expandTreeView(child);
            }
        }
    }

    private int safeParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            AnchorPane p = anchorpaneAdminReview;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("barangay.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class FetchReviewsTask extends Task<TreeItem<String>> {
        @Override
        protected TreeItem<String> call() throws Exception {
            TreeItem<String> pseudoRoot = new TreeItem<>("Root");


            String getReviewsQuery =
                    "SELECT b.barangay_name, p.place_name, r.rating, r.comment, r.review_id, a.username " +
                            "FROM tblReviews r " +
                            "JOIN tblPlace p ON r.place_id = p.place_id " +
                            "JOIN tblBarangay b ON p.barangay_id = b.barangay_id " +
                            "JOIN tblAccount a ON r.acc_id = a.acc_id " +
                            "WHERE p.isApproved = 1 ";

            if ("Unchecked".equals(chosenReviewStatus)) {
                hideButtons(true);
                getReviewsQuery += "AND r.isApproved = 0 AND r.isDeleted = 0 ";
            } else if ("Approved".equals(chosenReviewStatus)) {
                hideButtons(false);
                getReviewsQuery += "AND r.isApproved = 1 ";
            } else if ("Deleted".equals(chosenReviewStatus)) {
                getReviewsQuery += "AND r.isDeleted = 1 ";
                hideButtons(false);
            }

            getReviewsQuery += "ORDER BY b.barangay_name, p.place_name ";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(getReviewsQuery)) {

                ResultSet queryOutput = statement.executeQuery();
                TreeItem<String> barangayItem = null;
                TreeItem<String> placeItem = null;
                String currentBarangay = "";
                String currentPlace = "";
                int currentBarangayCount = 0;
                int currentPlaceCount = 0;

                while (queryOutput.next()) {
                    String barangayName = queryOutput.getString("barangay_name");
                    String placeName = queryOutput.getString("place_name");
                    // Add new data storage here
                    String reviewId = queryOutput.getString("review_id");
                    String username = queryOutput.getString("username");
                    float rating = queryOutput.getFloat("rating");
                    String comment = queryOutput.getString("comment");

                    reviews.add(new Review(reviewId, placeName, username, rating, comment));

                    // Changing barangay
                    if (!barangayName.equals(currentBarangay)) {

                        // Update previous barangayItem with review count
                        if (barangayItem != null) {
                            barangayItem.setValue(barangayItem.getValue() + " (" + currentBarangayCount + " review(s))");
                        }

                        // reset barangay review count
                        currentBarangayCount = 0;

                        // Create new BarangayItem
                        barangayItem = new TreeItem<>(barangayName);
                        pseudoRoot.getChildren().add(barangayItem);
                        currentBarangay = barangayName;
                        currentPlace = "";
                    }

                    // Changing place
                    if (!placeName.equals(currentPlace)) {

                        // Update previous placeItem with review count
                        if (placeItem != null) {
                            placeItem.setValue(placeItem.getValue() + " (" + currentPlaceCount + " review(s))");
                        }

                        // reset place review count
                        currentPlaceCount = 0;

                        // Create new PlaceItem
                        placeItem = new TreeItem<>(placeName);
                        barangayItem.getChildren().add(placeItem);
                        currentPlace = placeName;
                    }

                    // Adding review to a place
                    if (placeItem != null) {
                        String reviewID = "Review#" + queryOutput.getString("review_id");
                        placeItem.getChildren().add(new TreeItem<>(reviewID));
                        currentPlaceCount++;
                        currentBarangayCount++;
                    }
                }

                // Update the last placeItem and barangayItem counts
                if (placeItem != null) {
                    placeItem.setValue(placeItem.getValue() + " (" + currentPlaceCount + " review(s))");
                }
                if (barangayItem != null) {
                    barangayItem.setValue(barangayItem.getValue() + " (" + currentBarangayCount + " review(s))");
                }

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }

            expandTreeView(pseudoRoot);

            return pseudoRoot;
        }
    }

    private class FetchPlacesTask extends Task<TreeItem<String>> {

        private final boolean fetchApproved;
        private final boolean fetchDeleted;

        public FetchPlacesTask(boolean fetchApproved, boolean fetchDeleted) {
            this.fetchApproved = fetchApproved;
            this.fetchDeleted = fetchDeleted;
        }


        @Override
        protected TreeItem<String> call() throws Exception {
            TreeItem<String> pseudoRoot = new TreeItem<>("Root");

            String getPlacesQuery =
                    "SELECT b.barangay_name, p.place_name, p.place_id, a.username, p.place_about " +
                            "FROM tblPlace p " +
                            "JOIN tblBarangay b ON p.barangay_id = b.barangay_id " +
                            "JOIN tblAccount a ON p.acc_id = a.acc_id ";

            if (fetchApproved) {
                hideButtons(false);
                getPlacesQuery += "WHERE p.isApproved = 1 ";  // Only approved places

            } else if (fetchDeleted) {
                hideButtons(false);
                getPlacesQuery += "WHERE p.isDeleted = 1 ";  // Only deleted places

            } else {
                getPlacesQuery += "WHERE p.isApproved = 0 AND p.isDeleted = 0 ";  // No additional filtering (unapproved & non-deleted)
            }

            getPlacesQuery += "ORDER BY b.barangay_name, p.place_name ";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(getPlacesQuery)) {

                ResultSet queryOutput = statement.executeQuery();
                TreeItem<String> barangayItem = null;
                TreeItem<String> placeItem = null;
                String currentBarangay = "";
                String currentPlace = "";

                int currentBarangayCount = 0;
                int currentPlaceCount = 0;


                while (queryOutput.next()) {
                    String barangayName = queryOutput.getString("barangay_name");
                    String placeName = queryOutput.getString("place_name");
                    String placeId = queryOutput.getString("place_id");
                    String username = queryOutput.getString("username");
                    String about = queryOutput.getString("place_about");

                    places.add(new Places(barangayName, placeId, placeName, username, about));



                    if (!barangayName.equals(currentBarangay)) {
                        if (barangayItem != null) {
                            barangayItem.setValue(barangayItem.getValue() + " (" + currentBarangayCount + " place(s))");
                        }
                        currentBarangayCount = 0;
                        barangayItem = new TreeItem<>(barangayName);
                        pseudoRoot.getChildren().add(barangayItem);
                        currentBarangay = barangayName;
                        currentPlace = "";
                    }

                    // Changing place
                    if (!placeName.equals(currentPlace)) {

                        // Update previous placeItem with place count
                        if (placeItem != null) {
                            placeItem.setValue(placeItem.getValue() + " (" + currentPlaceCount + " place(s))");
                        }

                        // reset place place count
                        currentPlaceCount = 0;

                        // Create new PlaceItem
                        placeItem = new TreeItem<>(placeName);
                        barangayItem.getChildren().add(placeItem);
                        currentPlace = placeName;
                    }


                    if (placeItem != null) {
                        String placeID = "Place#" + placeId;
                        placeItem.getChildren().add(new TreeItem<>(placeID));
                        currentPlaceCount++;
                        currentBarangayCount++;
                    }
                }

                if (placeItem != null) {
                    placeItem.setValue(placeItem.getValue() + " (" + currentPlaceCount + " place(s))");
                }

                if (barangayItem != null) {
                    barangayItem.setValue(barangayItem.getValue() + " (" + currentBarangayCount + " place(s))");
                }

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }

            expandTreeView(pseudoRoot);

            return pseudoRoot;
        }
    }

    private void loadPlaceDetails(String selectedPlace) {


        try (Connection connectDB = MySQLConnection.ds.getConnection()) {
            String getDetailsQuery = "SELECT p.place_id, p.place_name, a.username, p.place_about " +
                    "FROM tblPlace p " +
                    "JOIN tblAccount a ON p.acc_id = a.acc_id " +
                    "WHERE p.place_id = ?";

            PreparedStatement statement = connectDB.prepareStatement(getDetailsQuery);
            String placeID = selectedPlace.replace("Place#", "");
            statement.setString(1, placeID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lblReviewID.setText("Place ID: " + resultSet.getString("place_id"));
                lblPlace.setText("Place: " + resultSet.getString("place_name"));
                lblRating.setText("Added by: " + resultSet.getString("username"));
                lblUsername.setText("About");  // No rating for places
                txtComment.setVisible(true);
                txtComment.setText(resultSet.getString("place_about")); // No comment for places
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private void loadReviewDetails(String selectedReview) {
        try (Connection connectDB = MySQLConnection.ds.getConnection()) {
            String getDetailsQuery = "SELECT r.review_id, p.place_name, a.username, r.rating, r.comment " +
                    "FROM tblReviews r " +
                    "JOIN tblPlace p ON r.place_id = p.place_id " +
                    "JOIN tblAccount a ON r.acc_id = a.acc_id " +
                    "WHERE r.review_id = ?";

            PreparedStatement statement = connectDB.prepareStatement(getDetailsQuery);
            String reviewId = selectedReview.replace("Review#", "");
            statement.setString(1, reviewId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lblReviewID.setText("Review ID: " + resultSet.getString("review_id"));
                lblPlace.setText("Place: " + resultSet.getString("place_name"));
                lblUsername.setText("User: " + resultSet.getString("username"));
                lblRating.setText("Rating: " + resultSet.getFloat("rating"));
                txtComment.setVisible(true);
                txtComment.setText(resultSet.getString("comment"));
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    @FXML
    public void hideButtons(boolean show){

        btnDeny.setVisible(show);
        btnApprove.setVisible(show);
        btnNext.setVisible(show);
        btnPrev.setVisible(show);
        txtComment.setVisible(show);
    }

    @FXML
    public void handlePrev() {
        if (currentReviewIndex > 0 && "Unchecked".equals(chosenReviewStatus)) {
            do {
                currentReviewIndex--;
            } while (currentReviewIndex >= 0 && isReviewAccessible(reviews.get(currentReviewIndex)));
            updateReviewDetails();
        }
        if (currentPlaceIndex > 0 && "New Places (Unchecked)".equals(chosenReviewStatus)) {
            do {
                currentPlaceIndex--;
            } while (currentPlaceIndex > 0 && isPlaceAccessible(places.get(currentPlaceIndex)));
            updatePlaceDetails();
        }
    }

    @FXML
    public void handleNext() {
        if (currentReviewIndex < (reviews.size() - 1) && "Unchecked".equals(chosenReviewStatus)) {
            do {
                currentReviewIndex++;
            } while (currentReviewIndex <= (reviews.size() - 1) && isReviewAccessible(reviews.get(currentReviewIndex)));
            updateReviewDetails();
        }

        if (currentPlaceIndex < (places.size() - 1) && "New Places (Unchecked)".equals(chosenReviewStatus)) {
            do {
                currentPlaceIndex++;
            } while (currentPlaceIndex <= (places.size() - 1) && isPlaceAccessible(places.get(currentPlaceIndex)));
            updatePlaceDetails();
        }
    }

    private void updateReviewDetails() {
        if (currentReviewIndex >= 0 && currentReviewIndex < reviews.size()) {
            Review review = reviews.get(currentReviewIndex);
            lblReviewID.setText("Review ID: " + review.reviewID);
            lblPlace.setText("Place: " + review.place);
            lblUsername.setText("User: " + review.username);
            lblRating.setText("Rating: " + review.rating);
            txtComment.setText(review.comment);
        } else {
            clearDetails();
        }
    }
    private void updatePlaceDetails() {
        if (currentPlaceIndex >= 0 && currentPlaceIndex < places.size()) {
            Places place = places.get(currentPlaceIndex);
            lblReviewID.setText("Place ID: " + place.getPlaceId());
            lblPlace.setText("Place: " + place.getplaceName());
            lblUsername.setText("User: " + place.getUsername());
            txtComment.setText(place.getAbout());
        } else {
            clearDetails();
        }
    }

    private void clearDetails() {
        lblReviewID.setText("");
        lblPlace.setText("");
        lblUsername.setText("");
        lblRating.setText("");
        txtComment.setText("");
    }
    private boolean isReviewAccessible(Review review) {
        try (Connection connectDB = MySQLConnection.ds.getConnection()) {
            String checkQuery = "SELECT isApproved, isDeleted FROM tblReviews WHERE review_id = ?";
            PreparedStatement statement = connectDB.prepareStatement(checkQuery);
            statement.setString(1, review.getReviewID());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("isApproved") || resultSet.getBoolean("isDeleted");
            }

        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return true; // by default, assume non-accessible
    }

    public void refreshTreeView() {
        Task<TreeItem<String>> fetchTask = null;
        String selectedItem = cbAdminReview.getSelectionModel().getSelectedItem();
        if("Reviews (Unchecked)".equals(selectedItem) || "Reviews (Approved)".equals(selectedItem) || "Reviews (Deleted)".equals(selectedItem)) {
            fetchTask = new FetchReviewsTask();
        } else if ("New Places (Unchecked)".equals(selectedItem)) {
            fetchTask = new FetchPlacesTask(false, false);
        } else if ("New Places (Approved)".equals(selectedItem)) {
            fetchTask = new FetchPlacesTask(true, false);
        } else if("New Places (Deleted)".equals(selectedItem)) { // New Places (Deleted)
            fetchTask = new FetchPlacesTask(false, true);
        }
        treeviewReview.rootProperty().bind(fetchTask.valueProperty());
        new Thread(fetchTask).start();
    }

    @FXML
    public void handleRefresh() {
        refreshTreeView();
    }


    @FXML
    public void handleDeny() {
        String selectedItem = cbAdminReview.getSelectionModel().getSelectedItem();
        if (currentReviewIndex >= 0) {
            if ("Reviews (Unchecked)".equals(selectedItem)) {
                Review review = reviews.get(currentReviewIndex);
                String updateQuery = "UPDATE tblReviews SET isDeleted = 1 WHERE review_id = ?";

                try (Connection connectDB = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                    statement.setString(1, review.reviewID);
                    statement.executeUpdate();
                    reviews.remove(currentReviewIndex);
                    currentReviewIndex = Math.max(0, currentReviewIndex - 1);
                    updateReviewDetails();
                    refreshTreeView();

                } catch (SQLException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            } else if ("New Places (Unchecked)".equals(selectedItem)) {
                String selectedItemText = treeviewReview.getSelectionModel().getSelectedItem().getValue();
                String placeId = selectedItemText.replace("Place#", "").split(":")[0];
                String updateQuery = "UPDATE tblPlace SET isDeleted = 1 WHERE place_id = ?";

                try (Connection connectDB = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                    statement.setString(1, placeId);
                    statement.executeUpdate();


                    currentPlaceIndex = (currentPlaceIndex >= places.size()) ? places.size() - 1 : currentPlaceIndex; //Adjust currentPlaceIndex
                    updatePlaceDetails();

                    refreshTreeView();


                } catch (SQLException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
        }
        if ("New Places (Deleted)".equals(selectedItem)) {
            String selectedItemText = treeviewReview.getSelectionModel().getSelectedItem().getValue();
            String placeId = selectedItemText.replace("Place#", "").split(":")[0];
            String updateQuery = "UPDATE tblPlace SET isDeleted = 1 WHERE place_id = ?";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                statement.setString(1, placeId);
                statement.executeUpdate();

                refreshTreeView();

            } catch (SQLException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    @FXML
    public void handleApprove() {
        String selectedItem = cbAdminReview.getSelectionModel().getSelectedItem();
        if (currentReviewIndex >= 0) {
            if ("Reviews (Unchecked)".equals(selectedItem) ) {
                Review review = reviews.get(currentReviewIndex);
                String updateQuery = "UPDATE tblReviews SET isApproved = 1, comment = ? WHERE review_id = ?";

                try (Connection connectDB = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                    statement.setString(1, txtComment.getText());
                    statement.setString(2, review.reviewID);
                    statement.executeUpdate();

                    reviews.remove(currentReviewIndex);
                    currentReviewIndex = Math.min(reviews.size() - 1, currentReviewIndex);
                    updateReviewDetails();
                    refreshTreeView();

                } catch (SQLException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            } else if ("New Places (Unchecked)".equals(selectedItem)) {
                String selectedItemText = treeviewReview.getSelectionModel().getSelectedItem().getValue();
                String placeId = selectedItemText.replace("Place#", "").split(":")[0];
                String updateQuery = "UPDATE tblPlace SET isApproved = 1, place_about = ? WHERE place_id = ?";   // Set isApproved = 1

                try (Connection connectDB = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                    statement.setString(1, txtComment.getText()); // Use text from txtComment as place_about
                    statement.setString(2, placeId);

                    statement.executeUpdate();


                    places.remove(currentPlaceIndex);
                    currentPlaceIndex = (currentPlaceIndex >= places.size()) ? places.size() - 1 : currentPlaceIndex;
                    updatePlaceDetails();
                    refreshTreeView();
                } catch (SQLException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }


            }

        }
    }

    private boolean isPlaceAccessible(Places place) {
        try (Connection connectDB = MySQLConnection.ds.getConnection()) {
            String checkQuery = "SELECT isApproved, isDeleted FROM tblPlace WHERE place_id = ?";
            PreparedStatement statement = connectDB.prepareStatement(checkQuery);
            statement.setString(1, place.getPlaceId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("isApproved") || resultSet.getBoolean("isDeleted");
            }

        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return true; // by default, assume non-accessible
    }



}




