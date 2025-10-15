package com.example.Service.User;

import com.example.DAO.UserDAO;
import com.example.Model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO;

    public UserService(Connection conn) {
        this.userDAO = new UserDAO(conn);
    }

    public User getUserProfile(int userId) {
        try {
            return userDAO.getUserProfile(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByUsername(String username) {
        User user = userDAO.getUserByUsername(username);
        System.out.println("Get user in User Serivce: " + user);

        return user;
    }


    /**
     * Wrapper to check if user exists by email.
     */
    public boolean existsByEmail(String email) {
        boolean userExist = userDAO.existsByEmail(email);
        System.out.println("Check user exist by email in User Serivce: " + userExist);

        return userExist;
    }

    /**
     * Wrapper to check if user exists by username.
     */
    public boolean existsByUsername(String username) {
        boolean userExist = userDAO.existsByUsername(username);
        System.out.println("Check user exist by username in User Serivce: " + userExist);

        return userExist;
    }

    public boolean insertNewUser(String username, String email, String password, int roleId) {
        return userDAO.insertNewUser(username, email, password, roleId);
    }

    public boolean getUserByUsernameAndPassword(String username, String password) {
        return userDAO.getUserByUsernameAndPassword(username, password);
    }
}
