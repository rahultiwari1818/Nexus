package com.Nexus_Library.dao;


import com.Nexus_Library.config.DBConnection;
import java.sql.*;

public class BookDAO {
    public void printAllBooks() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                System.out.println(rs.getString("title"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
