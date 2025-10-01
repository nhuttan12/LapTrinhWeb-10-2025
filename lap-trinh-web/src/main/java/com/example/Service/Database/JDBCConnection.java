package com.example.Service.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/lap-trinh-web";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123123";

    static {
        try {
            /*
             * Load driver
             */
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgresSQL JDBC Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
