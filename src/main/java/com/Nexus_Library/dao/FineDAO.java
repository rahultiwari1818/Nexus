package com.Nexus_Library.dao;

import java.sql.*;
import java.util.*;
import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Fine;

public class FineDAO {
    public List<Fine> getAllFines() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM fine");
        List<Fine> fines = new ArrayList<>();
        while (rs.next()) {
            fines.add(new Fine(
                    rs.getInt("fine_id"),
                    rs.getInt("loan_id"),
                    rs.getDouble("fine_amount"),
                    rs.getString("fine_status"),
                    rs.getString("payment_status")
            ));
        }
        return fines;
    }
}
