package com.example.munch_map;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Editprofile {
    @FXML
    AnchorPane anchorPaneEdit;
    @FXML
    TextField txtfieldUsername, txtfieldEmail;
    @FXML
    PasswordField txtfieldPassword;

    @FXML
    public void initialize() {
        if(Login.activeAdmin != null) {
            txtfieldUsername.setText(Login.activeAdmin.getUsername());
            txtfieldEmail.setText(Login.activeAdmin.getEmail());
        }
        else {
            txtfieldUsername.setText(Login.activeUser.getUsername());
            txtfieldEmail.setText(Login.activeUser.getEmail());
        }
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            AnchorPane p = anchorPaneEdit;
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("profile.fxml")));
            p.getScene().getStylesheets().clear();
            p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
            p.getChildren().clear();
            p.getChildren().add(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSave(ActionEvent actionEvent) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {
                try (Connection c = MySQLConnection.ds.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "UPDATE tblAccount SET username=?, email=?, password=? WHERE username=? AND email=?"
                     )) {

                    PreparedStatement checker = c.prepareStatement("SELECT * FROM tblAccount WHERE username = ? AND username != ? OR email = ? AND email != ?");

                    if(Login.activeAdmin != null) {
                        checker.setString(1, txtfieldUsername.getText());
                        checker.setString(2, Login.activeAdmin.getUsername());
                        checker.setString(3, txtfieldEmail.getText());
                        checker.setString(4, Login.activeAdmin.getEmail());
                    }
                    else {
                        checker.setString(1, txtfieldUsername.getText());
                        checker.setString(2, Login.activeUser.getUsername());
                        checker.setString(3, txtfieldEmail.getText());
                        checker.setString(4, Login.activeUser.getEmail());
                    }

                    ResultSet list = checker.executeQuery();

                    if(list.next()){
                        throw new SQLException("Username or Email Already Exists");
                    } else {
                        if (Login.activeUser != null) {
                            statement.setString(1, txtfieldUsername.getText());
                            statement.setString(2, txtfieldEmail.getText());
                            statement.setString(3, txtfieldPassword.getText());
                            statement.setString(4, Login.activeUser.getUsername());
                            statement.setString(5, Login.activeUser.getEmail());
                        } else {
                            statement.setString(1, txtfieldUsername.getText());
                            statement.setString(2, txtfieldEmail.getText());
                            statement.setString(3, txtfieldPassword.getText());
                            statement.setString(4, Login.activeAdmin.getUsername());
                            statement.setString(5, Login.activeAdmin.getEmail());
                        }
                        statement.executeUpdate();
                    }
                }
                return null;
            }
        };

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                System.out.println("Error: Username or Email already exists.");
            });
        });

        task.setOnSucceeded(e -> {
            try {
                AnchorPane p = anchorPaneEdit;
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("profile.fxml")));
                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
                p.getChildren().clear();
                p.getChildren().add(scene);
            } catch (IOException x) {
                x.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
