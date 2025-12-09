package com.example.Controller.User.Profile;

import com.example.DTO.Users.UserProfileDTO;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Image.ImageService;
import com.example.Service.User.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class UserProfileController extends HttpServlet {
    private UserService userService;
    private ImageService imageService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            userService = new UserService();
            imageService = new ImageService();
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        System.out.println("User id in session" + userId);

        UserProfileDTO profile = userService.getUserProfile(userId);

        if (profile != null) {
            req.setAttribute("userProfile", profile);
            req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");

        // 1. Get parameter from form
        String fullName = Objects.requireNonNullElse(req.getParameter("fullName"), "");
        String phone = Objects.requireNonNullElse(req.getParameter("phone"), "");
        String address1 = Objects.requireNonNullElse(req.getParameter("address1"), "");
        String address2 = Objects.requireNonNullElse(req.getParameter("address2"), "");
        String address3 = Objects.requireNonNullElse(req.getParameter("address3"), "");

        System.out.println("fullname " + fullName +
                " phone " + phone +
                " address 1 " + address1 +
                " address 2 " + address2 +
                " address 3 " + address3);

        // 2. Upload image path
        String imagePath = imageService.upload(req, "avatar");

        // 3. Update user profile
        UserProfileDTO profile = userService.updateUserProfile(userId, fullName, phone, address1, address2, address3, imagePath);

        // 4. Check user profile updated successfully
        if (profile != null) {
            req.setAttribute("userProfile", profile);
            req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }
}
