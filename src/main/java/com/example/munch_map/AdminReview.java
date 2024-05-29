package com.example.munch_map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class AdminReview {
    @FXML
    private ChoiceBox<String> cbAdminReview;

    @FXML
    private ListView<String> listviewReviews;

    @FXML
    public void initialize() {
        // Initialize the ComboBox or other components if needed
        cbAdminReview.getItems().addAll("Reviews", "Added Places");

        //FetchReviewTask
        Task<ObservableList<String>> fetchReviewsTask = new FetchReviewsTask();
        listviewReviews.itemsProperty().bind(fetchReviewsTask.valueProperty());

        new Thread(fetchReviewsTask).start();
    }

    private class FetchReviewsTask extends Task<ObservableList<String>> {
        @Override
        protected ObservableList<String> call() throws Exception {
            ObservableList<String> reviews = FXCollections.observableArrayList();

            String getReviewsQuery = "SELECT b.barangay_name, p.place_name, r.comment " +
                    "FROM tblReviews r " +
                    "JOIN tblPlace p ON r.place_id = p.place_id " +
                    "JOIN tblBarangay b ON p.barangay_id = b.barangay_id " +
                    "WHERE r.isApproved = 0 AND r.isDeleted = 0 " +
                    "ORDER BY b.barangay_name, p.place_name";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(getReviewsQuery)) {

                ResultSet queryOutput = statement.executeQuery();

                while(queryOutput.next()) {
                    String barangayName = queryOutput.getString("barangay_name");
                    String placeName = queryOutput.getString("place_name");
                    String review = queryOutput.getString("comment");

                    reviews.add(barangayName + "\n- " + placeName + "\n    :" + review);
                }

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
            return reviews;
        }
    }

}


