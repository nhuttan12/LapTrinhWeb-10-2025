package com.example.Service.Auth;

import com.example.Model.User;
import com.example.Service.User.UserService;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthService {
    private final UserService userService;

    public AuthService() {
        userService = new UserService();
    }

    /**
     * Business logic for login.
     * Service can add validation, logging, or transformation later.
     *
     * @param username username
     * @param password password
     * @return true if login success
     */
    public boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        return userService.getUserByUsernameAndPassword(username, password);
    }

    /**
     * Business logic for sign up.
     * - Check if username or email already exists.
     * - If not, insert new user.
     */
    public boolean signUp(String username, String email, String password) {
        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            return false;
        }

        if (existsByUsername(username)) {
            System.out.println("Tài khoản đã có người sử dụng.");
            return false;
        }

        if (existsByEmail(email)) {
            System.out.println("Email đã có người sử dụng.");
            return false;
        }

        return userService.insertNewUser(username, email, password);
    }

    /**
     * Wrapper to check if user exists by email.
     */
    public boolean existsByEmail(String email) {
        return userService.existsByEmail(email);
    }

    /**
     * Wrapper to check if user exists by username.
     */
    public boolean existsByUsername(String username) {
        return userService.existsByUsername(username);
    }

    public boolean changePassword(String username, String password, String newPassword) {
        return userService.changePassword(username, password, newPassword);
    }
}
