package src;

import java.sql.*;

public class DataBaseConfig {
    private static DataBaseConfig instance;
    private Connection connection;

    private DataBaseConfig() {
        try {
            String dbUrl = "jdbc:postgresql://localhost:5432/nexus_library";
            connection = DriverManager.getConnection(dbUrl, "postgres", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to PostgreSQL database");
        }
    }

    public static DataBaseConfig getInstance() {
        if (instance == null) {
            synchronized (DataBaseConfig.class) {
                if (instance == null) {
                    instance = new DataBaseConfig();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
