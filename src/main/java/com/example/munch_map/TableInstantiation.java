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
                    "password VARCHAR(100) NOT NULL," +
                    "admin_status TINYINT(1) NOT NULL DEFAULT 0)";

            String createBarangayTableQuery = "CREATE TABLE IF NOT EXISTS tblBarangay(" +
                    "barangay_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "barangay_name VARCHAR(255) NOT NULL UNIQUE)";

            String createPlaceTableQuery = "CREATE TABLE IF NOT EXISTS tblPlace (" +
                    "place_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "barangay_id INT(10) NOT NULL, " +
                    "place_name VARCHAR(255) NOT NULL, " +
                    "place_type VARCHAR(255) NOT NULL," +
                    "place_address VARCHAR(255) NOT NULL," +
                    "place_landmark VARCHAR(255) NOT NULL," +
                    "place_about VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (barangay_id) REFERENCES tblBarangay(barangay_id) ON DELETE CASCADE)";

            String createReviewTableQuery = "CREATE TABLE IF NOT EXISTS tblReviews (" +
                    "review_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "place_id INT(10) NOT NULL," +
                    "acc_id INT NOT NULL," +
                    "rating DECIMAL(2,1) NOT NULL CHECK (rating >= 0.0 AND rating <= 5.0)," +
                    "comment VARCHAR(255)," +
                    "FOREIGN KEY (place_id) REFERENCES tblPlace(place_id) ON DELETE CASCADE," +
                    "FOREIGN KEY (acc_id) REFERENCES tblAccount(acc_id) ON DELETE CASCADE," +
                    "isApproved TINYINT(1) NOT NULL DEFAULT 0)";

            // Query Executions
            statement.execute(createAccountTableQuery);
            statement.execute(createBarangayTableQuery);
            statement.execute(createPlaceTableQuery);
            statement.execute(createReviewTableQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
