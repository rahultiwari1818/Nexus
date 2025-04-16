package com.Nexus_Library.app;

import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.dao.ResourceDAO;
import com.Nexus_Library.dao.NotificationDAO;
import com.Nexus_Library.dao.TransactionDAO;
import com.Nexus_Library.model.User;
import com.Nexus_Library.model.Resource;
import com.Nexus_Library.model.Notification;
import com.Nexus_Library.model.Transaction;
import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();
            ResourceDAO resourceDAO = new ResourceDAO();
            NotificationDAO notificationDAO = new NotificationDAO();
            TransactionDAO transactionDAO = new TransactionDAO();

            List<User> users = userDAO.getAllUsers();
            List<Resource> resources = resourceDAO.getAllResources();
            List<Notification> notifications = notificationDAO.getAllNotifications();
            List<Transaction> transactions = transactionDAO.getAllTransactions();

            System.out.println("Users: ");
            for (User user : users) System.out.println(user);
            System.out.println("\nResources: ");
            for (Resource resource : resources) System.out.println(resource);
            System.out.println("\nNotifications: ");
            for (Notification notification : notifications) System.out.println(notification);
            System.out.println("\nTransactions: ");
            for (Transaction transaction : transactions) System.out.println(transaction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}