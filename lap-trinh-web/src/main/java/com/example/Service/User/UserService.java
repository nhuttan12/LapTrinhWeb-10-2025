package com.example.Service.User;

import com.example.DAO.UserDAO;
import com.example.DTO.Users.UserChangePasswordResponseDTO;
import com.example.DTO.Users.UserProfileDTO;
import com.example.Mappers.UserMapper;
import com.example.Model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UserService(Connection conn) {
        this.userDAO = new UserDAO(conn);
        this.userMapper = UserMapper.INSTANCE;
    }

    public UserProfileDTO getUserProfile(int userId) {
        try {
            User user = userDAO.getUserProfile(userId);
            if (user == null) {
                throw new SQLException("User not found");
            }

            UserProfileDTO result = userMapper.toUserProfileDTO(user);
            System.out.println("Get user profile in User Serivce: " + result);

            return result;
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

    public UserProfileDTO updateUserProfile(
            int userId,
            String fullName,
            String phone,
            String address1,
            String address2,
            String address3,
            String imageUrl) throws SQLException {

        // 1. Get user profile for double check
        UserProfileDTO profile = this.getUserProfile(userId);

        // 2. Update avatar to old avatar if no have new image
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = profile.getAvatar();
            profile.setAvatar(imageUrl);
        }

        // 3. Update user profile
        boolean updateUserProfile = userDAO.updateUserProfile(profile.getId(), fullName, phone, address1, address2, address3, imageUrl);

        // 4. Checking update result
        if (!updateUserProfile) {
            throw new SQLException("Update user profile failed");
        }

        // 5. Get user profile for return
        return this.getUserProfile(userId);
    }

    public UserChangePasswordResponseDTO getUserChangePasswordResponse(int userId) {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                throw new SQLException("User not found");
            }

            UserChangePasswordResponseDTO result = userMapper.toUserChangePasswordResponseDto(user);
            System.out.println("Get user change password response in User Serivce: " + result);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    public boolean changePassword(String username, String password, String newPassword) {
//        System.out.println("Info username: " + username + " password: " + password);
        boolean userExist = userDAO.getUserByUsernameAndPassword(username, password);
        System.out.println("Check user exist in user service: " + userExist);

        if (!userExist) {
            return false;
        }

        return userDAO.changePassword(username, newPassword);
    }
}
