package com.Nexus_Library.app;
import com.Nexus_Library.config.DBConnection;
import java.sql.Connection;

public class MainApp {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Connection successful: " + conn);
        } else {
            System.out.println("Connection failed.");
        }
        //DBConnection.closeConnection();
    }
}
