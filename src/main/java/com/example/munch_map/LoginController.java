package com.example.munch_map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    TextField useremailInput;
    PasswordField passwordInput;
    AnchorPane LoginPage;

    static int currentUser;
    public void onLoginButtonClick() throws IOException {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {

            String query = "SELECT * FROM tblAccount";
            ResultSet list = statement.executeQuery(query);

            while (list.next()) {
                if ((useremailInput.getText().equals(list.getString("username")) ||
                        useremailInput.getText().equals(list.getString("email"))) &&
                        passwordInput.getText().equals(list.getString("password"))) {
                    currentUser = list.getInt("acc_id");
                    AnchorPane p = LoginPage;
                    // TOD0: Link to MunchMap Main Page and CSS
                    Parent scene = FXMLLoader.load(getClass().getResource("MunchMap.fxml"));
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(getClass().getResource("munchmap.css").toExternalForm());
                    p.getChildren().clear();
                    p.getChildren().add(scene);
                }
            }
            // TOD0: Add error message
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
