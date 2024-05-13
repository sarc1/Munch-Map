package com.example.munch_map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class Barangay {

    // Pushing for rename, until now I don't understand ngano ni ang fxml haha -rei
    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private void onComboBoxAction(ActionEvent event) {
        String selectedItem = comboBox.getSelectionModel().getSelectedItem();
        // Handle the selected item
    }

    @FXML
    private void openNewWindow(ActionEvent event) {
        // Logic to open new window
    }
}
