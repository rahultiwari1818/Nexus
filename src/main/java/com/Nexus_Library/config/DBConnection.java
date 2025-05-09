package com.Nexus_Library.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties prop = new Properties();
    private static Connection connection;

    static {
        // Load properties file once during class initialization
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Property file 'db.properties' not found in the classpath");
            }
            prop.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    private DBConnection() {}

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.username");
                String pass = prop.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, pass);
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
}
