package com.example.munch_map;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableInstantiation {
    public static void initializeTables() {

        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {

            // Table Queries
            String createAccountTableQuery = "CREATE TABLE IF NOT EXISTS tblAccount(" +
                    "acc_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," +
                    "password VARCHAR(100) NOT NULL)";

            String createBarangayTableQuery = "CREATE TABLE IF NOT EXISTS tblBarangay(" +
                    "barangay_name VARCHAR(100) NOT NULL PRIMARY KEY)";
            //TOD0: insert Location into tblBarangay (Coordinates)

            // Query Executions
            statement.execute(createAccountTableQuery);
            statement.execute(createBarangayTableQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
