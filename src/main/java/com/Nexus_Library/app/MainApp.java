package com.Nexus_Library.app;

public class MainApp {
    public static void main(String[] args) {
        BookDAO bookDao = new BookDAO();
        bookDao.printAllBooks();
    }
}
