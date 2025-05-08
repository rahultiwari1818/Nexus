package com.Nexus_Library.utils;

import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.*;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidRole(User loggedInUser) {
        return loggedInUser != null && Arrays.asList("Student", "Faculty", "Researcher").contains(loggedInUser.getRole());
    }



    public static String getCurrentExtraParam(LibraryItem item) {
        if (item instanceof EBook) return ((EBook) item).getFileFormat();
        if (item instanceof PhysicalBook) return String.valueOf(((PhysicalBook) item).getCopies());
        if (item instanceof ResearchPaper) return ((ResearchPaper) item).getPublication();
        if (item instanceof AudioBook) return String.valueOf(((AudioBook) item).getDurationMinutes());
        return null;
    }

    public static String getUpdatedExtraParam(LibraryItem item, String extraParam) {
        if ("physicalbook".equals(item.getItemType()) || "audiobook".equals(item.getItemType())) {
            try {
                int param = Integer.parseInt(extraParam);
                if (param <= 0) {
                    System.out.println("❌ Extra parameter must be a positive number.");
                    return null;
                }
                return extraParam;
            } catch (NumberFormatException e) {
                System.out.println("❌ Extra parameter must be a number.");
                return null;
            }
        }
        return extraParam;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) return false;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        return hasLetter && hasNumber;
    }

    public static boolean isEmailExists(UserDAO userDAO,String email) {
        try {
            return userDAO.loginUser(email, "") != null;
        } catch (Exception e) {
            System.out.println("❌ Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    public static boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.matches("[a-zA-Z\\s]+");
    }

    public static boolean isValidIsbn(String isbn) {
        return isbn != null && isbn.matches("\\d{13}");
    }



}
