package com.Nexus_Library.dao;

import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Metadata;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetadataDAO {
    public List<Metadata> getAllMetadata() throws SQLException {
        List<Metadata> metadata = new ArrayList<>();
        String query = "SELECT * FROM metadata";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                metadata.add(new Metadata(
                        rs.getInt("metadata_id"),
                        rs.getInt("resource_id"),
                        rs.getString("keywords"),
                        rs.getString("tags"),
                        rs.getDouble("rating")
                ));
            }
        }
        return metadata;
    }
}

//package com.Nexus_Library.dao;
//
//import java.sql.*;
//import java.util.*;
//import com.Nexus_Library.config.DBConnection;
//import com.Nexus_Library.model.Metadata;
//
//public class MetadataDAO {
//    public List<Metadata> getAllMetadata() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM metadata");
//        List<Metadata> metadataList = new ArrayList<>();
//        while (rs.next()) {
//            metadataList.add(new Metadata(
//                    rs.getInt("metadata_id"),
//                    rs.getInt("resource_id"),
//                    rs.getString("keywords"),
//                    rs.getString("tags"),
//                    rs.getDouble("rating")
//            ));
//        }
//        return metadataList;
//    }
//}
