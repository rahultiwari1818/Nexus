//package com.Nexus_Library;

import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.User;
import java.sql.SQLException;
import java.util.List;

public class MainProgram {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                System.out.println(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}