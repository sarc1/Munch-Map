package com.example.munch_map;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class Transition {

    public AnchorPane transitionPage;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private void initialize() {
    }

    public void setProgress(double value) {
        progressBar.setProgress(value);
    }
}
