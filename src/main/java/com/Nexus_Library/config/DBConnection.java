//package com.Nexus_Library.config;
//
//import java.io.FileNotFoundException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//import java.io.InputStream;
//
//
//public class DBConnection {
//    private static Connection connection;
//
//    private DBConnection() {}
//
//    public static Connection getConnection() {
//        if (connection == null) {
//            Properties prop = new Properties();
//            try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
//                if (input == null) {
//                    throw new FileNotFoundException("Property file 'db.properties' not found in the classpath");
//                }
//                prop.load(input);
//
//                String url = prop.getProperty("db.url");
//                String user = prop.getProperty("db.username");
//                String pass = prop.getProperty("db.password");
//
//                connection = DriverManager.getConnection(url, user, pass);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return connection;
//    }
//}
package com.Nexus_Library.config;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            Properties prop = new Properties();
            try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new FileNotFoundException("Property file 'db.properties' not found in the classpath");
                }
                prop.load(input);

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.username");
                String pass = prop.getProperty("db.password");

                // Attempt to establish connection
                connection = DriverManager.getConnection(url, user, pass);

                // Validate the connection
                if (connection != null && connection.isValid(2)) {
                    System.out.println("Database connection established successfully!");
                } else {
                    throw new SQLException("Connection is not valid.");
                }

            } catch (FileNotFoundException e) {
                System.err.println("Configuration error: " + e.getMessage());
                connection = null;
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                connection = null;
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                connection = null;
            }
        }
        return connection;
    }

    // Optional: Method to close connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}