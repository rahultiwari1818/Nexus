package com.Nexus_Library.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties prop = new Properties();

    static {
        // Load properties file once during class initialization
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Property file 'db.properties' not found in the classpath");
            }
            prop.load(input);
//            System.out.println("Properties loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String pass = prop.getProperty("db.password");

            Connection conn = DriverManager.getConnection(url, user, pass);
            if (conn.isClosed()) {
                throw new SQLException("Created connection is closed!");
            }
//            System.out.println("DB Connected");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
}