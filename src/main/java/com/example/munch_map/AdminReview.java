package com.example.munch_map;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminReview {
    @FXML
    private ChoiceBox<String> cbAdminReview;

    @FXML
    private TreeView<String> treeviewReview;

    @FXML
    public void initialize() {
        // Initialize the ComboBox or other components if needed
        cbAdminReview.getItems().addAll("Reviews", "Added Places");

        //FetchReviewTask
        Task<TreeItem<String>> fetchReviewsTask = new FetchReviewsTask();
        treeviewReview.rootProperty().bind(fetchReviewsTask.valueProperty());

        new Thread(fetchReviewsTask).start();
    }

    private class FetchReviewsTask extends Task<TreeItem<String>> {
        @Override
        protected TreeItem<String> call() throws Exception {

            TreeItem<String> pseudoRoot = new TreeItem<> ("Root");

            String getReviewsQuery =
                    "SELECT b.barangay_name, p.place_name, r.comment " +
                            "FROM tblReviews r " +
                            "JOIN tblPlace p ON r.place_id = p.place_id " +
                            "JOIN tblBarangay b ON p.barangay_id = b.barangay_id " +
                            "WHERE r.isApproved = 0 AND r.isDeleted = 0 " +
                            "ORDER BY b.barangay_name, p.place_name";

            try (Connection connectDB = MySQLConnection.ds.getConnection();
                 PreparedStatement statement = connectDB.prepareStatement(getReviewsQuery)) {

                ResultSet queryOutput = statement.executeQuery();
                TreeItem<String> barangayItem = null;
                TreeItem<String> placeItem = null;
                String currentBarangay = "";
                String currentPlace = "";

                while(queryOutput.next()) {
                    String barangayName = queryOutput.getString("barangay_name");
                    String placeName = queryOutput.getString("place_name");
                    String review = queryOutput.getString("comment");

                    if (!barangayName.equals(currentBarangay)) {
                        barangayItem = new TreeItem<>(barangayName);
                        pseudoRoot.getChildren().add(barangayItem);
                        currentBarangay = barangayName;
                        // Reset place for new barangay
                        currentPlace = "";
                    }

                    if (barangayItem != null && !placeName.equals(currentPlace)) {
                        placeItem = new TreeItem<>(placeName);
                        barangayItem.getChildren().add(placeItem);
                        currentPlace = placeName;
                    }

                    if (placeItem != null) {
                        placeItem.getChildren().add(new TreeItem<>(review));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
            return pseudoRoot;
        }
    }
}


