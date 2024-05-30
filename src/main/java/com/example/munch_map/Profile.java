package com.example.munch_map;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Profile {
    @FXML
    private Button btnEditProfile;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private AnchorPane AnchorPaneProfile;
    @FXML
    private Text errorText;

    public void onEditProfileButtonClick() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (Connection c = MySQLConnection.ds.getConnection()) {
                    int currentUserId = User.getUserSession().getUserId(); // Get current user ID

                    // Check for duplicate username
                    String checkUsernameQuery = "SELECT * FROM tblAccount WHERE username = ? AND acc_id != ?";
                    try (PreparedStatement checker = c.prepareStatement(checkUsernameQuery)) {
                        checker.setString(1, usernameInput.getText());
                        checker.setInt(2, currentUserId);
                        ResultSet resultSet = checker.executeQuery();

                        if (resultSet.next()) {
                            // Username already exists
                            Platform.runLater(() -> errorText.setText("Username already exists!"));
                        } else {
                            // Update user profile
                            String updateProfileQuery = "UPDATE tblAccount SET username = ?, email = ?, password = ? WHERE acc_id = ?";
                            try (PreparedStatement statement = c.prepareStatement(updateProfileQuery)) {
                                statement.setString(1, usernameInput.getText());
                                statement.setString(2, emailInput.getText());
                                statement.setString(3, passwordInput.getText());
                                statement.setInt(4, currentUserId);

                                int rowsUpdated = statement.executeUpdate();
                                if (rowsUpdated > 0) {
                                    // Update UserSession
                                    User.getUserSession().setUsername(usernameInput.getText());
                                    User.getUserSession().setEmail(emailInput.getText());

                                    Platform.runLater(() -> {
                                        // Show success message or navigate to another page
                                        errorText.setText("Profile updated successfully!");
                                    });
                                } else {
                                    Platform.runLater(() -> errorText.setText("Failed to update profile!"));
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            AnchorPane p = AnchorPaneProfile;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("barangay.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
