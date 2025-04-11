package com.Nexus_Library.dao;

import java.sql.*;
import java.util.*;
import com.Nexus_Library.config.DBConnection;
import com.Nexus_Library.model.Metadata;

public class MetadataDAO {
    public List<Metadata> getAllMetadata() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM metadata");
        List<Metadata> metadataList = new ArrayList<>();
        while (rs.next()) {
            metadataList.add(new Metadata(
                    rs.getInt("metadata_id"),
                    rs.getInt("resource_id"),
                    rs.getString("keywords"),
                    rs.getString("tags"),
                    rs.getDouble("rating")
            ));
        }
        return metadataList;
    }
}
