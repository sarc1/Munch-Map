package com.example.munch_map;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Login {
    public Button btnLogin;
    @FXML
    TextField useremailInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    AnchorPane LoginPage;
    static User activeUser;
    static Admin activeAdmin;

    public void onLoginButtonClick() throws IOException {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (Connection c = MySQLConnection.ds.getConnection(); Statement statement = c.createStatement()) {
                    String query = "SELECT * FROM tblAccount";
                    ResultSet list = statement.executeQuery(query);
                    while (list.next()) {
                        if ((useremailInput.getText().equals(list.getString("username")) || useremailInput.getText().equals(list.getString("email"))) && passwordInput.getText().equals(list.getString("password"))) {
                            if (list.getBoolean("admin_status")) {
                                activeAdmin = new Admin(list.getString("username"), list.getString("email"), true);
                            } else {
                                activeUser = new User(list.getString("username"), list.getString("email"), true);
                            }
                        }
                    }
                    // TODO: Add error message
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            try {
                AnchorPane p = LoginPage;
                Parent scene;
                if (activeAdmin != null) {
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin_review.fxml")));
                } else {
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Barangay.fxml")));
                }
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
}