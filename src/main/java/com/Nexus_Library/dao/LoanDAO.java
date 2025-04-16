package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loan";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("loan_id"),
                        rs.getInt("user_id"),
                        rs.getInt("resource_id"),
                        rs.getTimestamp("borrow_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getString("status")
                ));
            }
        }
        return loans;
    }
}

//package com.Nexus_Library.dao;
//
//import java.sql.*;
//import java.util.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.Loan;
//
//public class LoanDAO {
//    public List<Loan> getAllLoans() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM loan");
//        List<Loan> loans = new ArrayList<>();
//        while (rs.next()) {
//            loans.add(new Loan(
//                    rs.getInt("loan_id"),
//                    rs.getInt("user_id"),
//                    rs.getInt("resource_id"),
//                    rs.getDate("borrow_date"),
//                    rs.getDate("due_date"),
//                    rs.getDate("return_date"),
//                    rs.getString("status")
//            ));
//        }
//        return loans;
//    }
//}
