package com.example.munch_map;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableInstantiation {
    public static void initializeTables() {

        try (Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS tblAccount(" +
                "acc_id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "password VARCHAR(100) NOT NULL)";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
