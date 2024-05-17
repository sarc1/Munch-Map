package com.example.munch_map;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableInstantiation {
    public static void initializeTables() {

        try (Connection c = MySQLConnection.ds.getConnection();
             Statement statement = c.createStatement()) {

            // Table Queries
            String createAccountTableQuery = "CREATE TABLE IF NOT EXISTS tblAccount(" +
                    "acc_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," +
                    "password VARCHAR(100) NOT NULL)";

            String createBarangayTableQuery = "CREATE TABLE IF NOT EXISTS tblBarangay(" +
                    "barangay_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "barangay_name VARCHAR(255) NOT NULL UNIQUE)";

            //TOD0: insert Location into tblBarangay (Coordinates) for GMaps implementation

            String createPlaceTableQuery = "CREATE TABLE IF NOT EXISTS tblPlace (" +
                    "place_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "barangay_id INT(10) NOT NULL, " +
                    "place_name VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (barangay_id) REFERENCES tblBarangay(barangay_id) ON DELETE CASCADE)";

            // Query Executions
            statement.execute(createAccountTableQuery);
            statement.execute(createBarangayTableQuery);
            statement.execute(createPlaceTableQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
