package com.Nexus_Library.pattern.behavioral;

import com.Nexus_Library.dao.LibraryItemDAO;
import com.Nexus_Library.model.LibraryItem;

import java.util.List;

public class AuthorSearch implements SearchStrategy <LibraryItem>{
    @Override
    public List<LibraryItem> search(String query) throws Exception {
        LibraryItemDAO dao = new LibraryItemDAO();
        return dao.searchByAuthor(query);
    }
}