package com.Nexus_Library.app;

import com.Nexus_Library.dao.BookDAO;

public class MainApp {
    public static void main(String[] args) {
        BookDAO bookDao = new BookDAO();
        bookDao.printAllBooks();
    }
}
