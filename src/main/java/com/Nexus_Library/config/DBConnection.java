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

                connection = DriverManager.getConnection(url, user, pass);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
