package com.Nexus_Library.pattern.behavioral;

import com.Nexus_Library.model.LibraryItem;

import java.util.List;

public class SearchContext<T> {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<T> executeSearch(String query) throws Exception {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set.");
        }
        return strategy.search(query);
    }
}
