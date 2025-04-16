package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Reservation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("user_id"),
                        rs.getInt("resource_id"),
                        rs.getTimestamp("reservation_date"),
                        rs.getTimestamp("pickup_date"),
                        rs.getString("status")
                ));
            }
        }
        return reservations;
    }
}

//package com.Nexus_Library.dao;
//
//import java.sql.*;


//import java.util.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.Reservation;
//
//public class ReservationDAO {
//    public List<Reservation> getAllReservations() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM reservation");
//        List<Reservation> reservations = new ArrayList<>();
//        while (rs.next()) {
//            reservations.add(new Reservation(
//                    rs.getInt("reservation_id"),
//                    rs.getInt("user_id"),
//                    rs.getInt("resource_id"),
//                    rs.getTimestamp("reservation_date"),
//                    rs.getDate("pickup_date"),
//                    rs.getString("status")
//            ));
//        }
//        return reservations;
//    }
//}
