package com.Nexus_Library.pattern.behavioral;

import com.Nexus_Library.model.LibraryItem;

import java.util.List;

public interface SearchStrategy<T> {
    List<T> search(String query) throws Exception;
}
