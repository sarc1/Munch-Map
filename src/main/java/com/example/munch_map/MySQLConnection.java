package com.example.munch_map;

import com.zaxxer.hikari.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/munchmap";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    private static final HikariConfig config = new HikariConfig();
    public static final HikariDataSource ds;

    static {
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        ds = new HikariDataSource(config);
    }


    public static Connection getConnection() {
        Connection c = null;
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            c = DriverManager.getConnection(URL, USERNAME, PASSWORD)
            c = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void main(String[] args) {
        Connection c = getConnection();
    }
}