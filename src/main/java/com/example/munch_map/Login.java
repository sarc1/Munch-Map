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

public class Login {
    public Button btnLogin;
    @FXML
    TextField useremailInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    AnchorPane LoginPage;
    @FXML
    Text errorText;

    static User activeUser;
    static Admin activeAdmin;

    public void onLoginButtonClick() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {
                try (Connection c = MySQLConnection.ds.getConnection()) {
                    String query = "SELECT * FROM tblAccount WHERE (username = ? OR email = ?) AND password = ?";
                    PreparedStatement stmt = c.prepareStatement(query);
                    stmt.setString(1, useremailInput.getText());
                    stmt.setString(2, useremailInput.getText());
                    stmt.setString(3, passwordInput.getText());

                    try (ResultSet list = stmt.executeQuery()) {
                        while (list.next()) {
                            if (list.getBoolean("admin_status")) {
                                activeAdmin = new Admin(list.getString("username"), list.getString("email"), true);
                            } else {
                                activeUser = new User(list.getString("username"), list.getString("email"), true);
                            }
                        }
                    }

                    if (activeAdmin == null && activeUser == null) {
                        throw new SQLException("Incorrect Username or Password");
                    }
                }

                return null;
            }
        };

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                useremailInput.clear();
                passwordInput.clear();
                errorText.setText("Incorrect Username or Password");
            });
        });

        task.setOnSucceeded(event -> {
            try {
                AnchorPane p = LoginPage;
                Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Barangay.fxml")));

                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("barangay.css")).toExternalForm());
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
            AnchorPane p = LoginPage;
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