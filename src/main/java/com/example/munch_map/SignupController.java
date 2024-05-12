package com.example.munch_map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupController {
    Text caption;
    TextField usernameInput, emailInput;
    PasswordField passwordInput;
    AnchorPane SignUpPage;

    public void onSignUpButtonClick() throws IOException {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO tblAccount (username, email, password) VALUES (?, ?, ?)"
             )) {

            PreparedStatement checker = c.prepareStatement("SELECT * FROM tblAccount WHERE username = ? OR email = ?");

            checker.setString(1, usernameInput.getText());
            checker.setString(2, emailInput.getText());

            ResultSet list = checker.executeQuery();

            if(list.next()) {
                // TOD0: Implement Error Message
                System.out.println("Username or Email already exists");
            } else {
                statement.setString(1, usernameInput.getText());
                statement.setString(2, emailInput.getText());
                statement.setString(3, passwordInput.getText());
                statement.executeUpdate();

                AnchorPane p = SignUpPage;
                Parent scene = FXMLLoader.load(getClass().getResource("Login.fxml"));
                p.getScene().getStylesheets().clear();
                p.getScene().getStylesheets().add(getClass().getResource("login.css").toExternalForm());
                p.getChildren().clear();
                p.getChildren().add(scene);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
