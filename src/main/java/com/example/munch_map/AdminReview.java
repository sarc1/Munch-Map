package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
    private List<Review> reviews = new ArrayList<>();
    private int currentReviewIndex = -1;

    private String chosenReviewStatus = "";

    @FXML
    Button btnDeny, btnApprove, btnNext, btnPrev;

//    @FXML
//    private Button btnDeny, btnApprove;

    @FXML
    public void initialize() {
        // Initialize the ComboBox or other components if needed\

        cbAdminReview.getItems().addAll("Reviews (Unchecked)","Reviews (Approved)","Reviews (Deleted)","New Places (Unchecked)","New Places (Approved)","New Places (Deleted)");

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

            }
        });

        treeviewReview.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    String selectedReview = newValue.getValue();

                    if (selectedReview.startsWith("Review#")) {
                        try (Connection connectDB = MySQLConnection.ds.getConnection()) {
                            String getDetailsQuery = "SELECT r.review_id, p.place_name, a.username, r.rating, r.comment FROM tblReviews r"
                                    + " JOIN tblPlace p ON r.place_id = p.place_id"
                                    + " JOIN tblAccount a ON r.acc_id = a.acc_id"
                                    + " WHERE r.review_id = ?";

                            PreparedStatement statement = connectDB.prepareStatement(getDetailsQuery);
                            String reviewId = selectedReview.replace("Review#", "");
                            statement.setString(1, reviewId);
                            ResultSet resultSet = statement.executeQuery();
                            treeviewReview.setShowRoot(false);
                            txtComment.setVisible(true);

                            if (resultSet.next()) {
                                lblReviewID.setText("Review ID: " + resultSet.getString("review_id"));
                                lblPlace.setText("Place: " + resultSet.getString("place_name"));
                                lblUsername.setText("User: " + resultSet.getString("username"));
                                lblRating.setText("Rating: " + resultSet.getFloat("rating"));
                                txtComment.setText(resultSet.getString("comment"));
                            }
                        } catch (SQLException ex) {
                            System.err.println("Error: " + ex.getMessage());
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


    private class FetchReviewsTask extends Task<TreeItem<String>> {
        @Override
        protected TreeItem<String> call() throws Exception {
            TreeItem<String> pseudoRoot = new TreeItem<>("Root");

            String getReviewsQuery =
                    "SELECT b.barangay_name, p.place_name, r.rating, r.comment, r.review_id, a.username " +
                            "FROM tblReviews r " +
                            "JOIN tblPlace p ON r.place_id = p.place_id " +
                            "JOIN tblBarangay b ON p.barangay_id = b.barangay_id " +
                            "JOIN tblAccount a ON r.acc_id = a.acc_id";


            if ("Unchecked".equals(chosenReviewStatus)) {
                hideButtons(true);
                getReviewsQuery += " WHERE r.isApproved = 0 AND r.isDeleted = 0";
            } else if ("Approved".equals(chosenReviewStatus)) {
                hideButtons(false);
                getReviewsQuery += " WHERE r.isApproved = 1";
            } else if ("Deleted".equals(chosenReviewStatus)) {
                getReviewsQuery += " WHERE r.isDeleted = 1";
                hideButtons(false);
            }

            getReviewsQuery += " ORDER BY b.barangay_name, p.place_name";

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

    @FXML
    public void hideButtons(boolean show){

        btnDeny.setVisible(show);
        btnApprove.setVisible(show);
        btnNext.setVisible(show);
        btnPrev.setVisible(show);
    }

    @FXML
    public void handlePrev() {
        if (currentReviewIndex > 0 && "Unchecked".equals(chosenReviewStatus)) {
            do {
                currentReviewIndex--;
            } while (currentReviewIndex >= 0 && isReviewAccessible(reviews.get(currentReviewIndex)));
            updateReviewDetails();
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
    }


    private void updateReviewDetails() {
        if (currentReviewIndex >= 0 && currentReviewIndex < reviews.size()) {
            Review review = reviews.get(currentReviewIndex);
            lblReviewID.setText("Review ID: " + review.reviewID);
            lblPlace.setText("Place: " + review.place);
            lblUsername.setText("User: " + review.username);
            lblRating.setText("Rating: " + review.rating);
            txtComment.setText(review.comment);
        }
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
        Task<TreeItem<String>> fetchReviewsTask = new FetchReviewsTask();
        treeviewReview.rootProperty().bind(fetchReviewsTask.valueProperty());
        new Thread(fetchReviewsTask).start();
    }

    @FXML
    public void handleRefresh() {
        refreshTreeView();
    }


    @FXML
    public void handleDeny() {
        String selectedItem = cbAdminReview.getSelectionModel().getSelectedItem();
        if ("Reviews (Unchecked)".equals(selectedItem) && currentReviewIndex >= 0)  {
            Review review = reviews.get(currentReviewIndex);
            String updateQuery = "UPDATE tblReviews SET isDeleted = 1 WHERE review_id = ?";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                statement.setString(1, review.reviewID);
                statement.executeUpdate();
                reviews.remove(currentReviewIndex); // remove review from list
                currentReviewIndex = Math.max(0, currentReviewIndex - 1); // set index to next review or last review
                updateReviewDetails();
                //Refresh the TreeView if necessary
                refreshTreeView();

            } catch (SQLException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    @FXML
    public void handleApprove() {
        String selectedItem = cbAdminReview.getSelectionModel().getSelectedItem();
        if ("Reviews (Unchecked)".equals(selectedItem) && currentReviewIndex >= 0){
            Review review = reviews.get(currentReviewIndex);
            String updateQuery = "UPDATE tblReviews SET isApproved = 1, comment = ? WHERE review_id = ?";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(updateQuery)) {

                statement.setString(1, txtComment.getText());
                statement.setString(2, review.reviewID);
                statement.executeUpdate();

                reviews.remove(currentReviewIndex); // remove review from list
                currentReviewIndex = Math.min(reviews.size() - 1, currentReviewIndex); // set index to next review or last review
                updateReviewDetails();

                //Refresh the TreeView if necessary
                refreshTreeView();

            } catch (SQLException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

}




