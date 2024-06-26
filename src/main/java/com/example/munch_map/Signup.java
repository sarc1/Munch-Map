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

public class Signup {
    public Button btnSignUp;
    @FXML
    Text caption;
    @FXML
    TextField usernameInput, emailInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    AnchorPane SignUpPage;
    @FXML
    Text errorText;

    public void onSignUpButtonClick() {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                try (Connection c = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO tblAccount (username, email, password) VALUES (?, ?, ?)"
                     )) {

                    PreparedStatement checker = c.prepareStatement("SELECT * FROM tblAccount WHERE username = ? OR email = ?");

                    checker.setString(1, usernameInput.getText());
                    checker.setString(2, emailInput.getText());

                    ResultSet list = checker.executeQuery();

                    if (list.next()) {
                        // TOD0: Implement Error Message
                        throw new SQLException("Username or Email Already Exists");
                    } else {
                        statement.setString(1, usernameInput.getText());
                        statement.setString(2, emailInput.getText());
                        statement.setString(3, passwordInput.getText());
                        statement.executeUpdate();
                    }
                }
                return null;
            }
        };

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                errorText.setText("Username or Email Already Exists");
            });
        });

        task.setOnSucceeded(event -> {
            try {
                AnchorPane p = SignUpPage;
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
                p.getChildren().clear();
                p.getChildren().add(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            AnchorPane p = SignUpPage;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MunchMap.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("munchmap.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
