package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;


public class AdminReview {
    @FXML
    private ChoiceBox<String> cbAdminReview;

    @FXML
    public void initialize() {
        // Initialize the ComboBox or other components if needed
        cbAdminReview.getItems().addAll("Reviews", "Added Places");
    }
}
