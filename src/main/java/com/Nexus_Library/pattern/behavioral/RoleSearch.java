package com.Nexus_Library.pattern.behavioral;

import com.Nexus_Library.dao.UserDAO;
import com.Nexus_Library.model.User;

import java.util.List;

public class RoleSearch implements SearchStrategy<User>{
    @Override
    public List<User> search(String query) throws Exception {
        UserDAO userDAO = new UserDAO();
        return userDAO.searchByRole(query);    }
}
