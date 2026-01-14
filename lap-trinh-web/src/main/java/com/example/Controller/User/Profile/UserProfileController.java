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
import java.util.HashMap;
import java.util.Map;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        UserProfileDTO profile = userService.getUserProfile(userId);

        if (profile != null) {
            req.setAttribute("userProfile", profile);
            req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");

        // 1. Get form data
        String fullName = Objects.requireNonNullElse(req.getParameter("fullName"), "");
        String phone = Objects.requireNonNullElse(req.getParameter("phone"), "");
        String address1 = Objects.requireNonNullElse(req.getParameter("address1"), "");
        String address2 = Objects.requireNonNullElse(req.getParameter("address2"), "");
        String address3 = Objects.requireNonNullElse(req.getParameter("address3"), "");

        Map<String, String> errors = new HashMap<>();

        if (fullName.isEmpty()) {
            errors.put("fullName", "Họ tên không được để trống");
        }

        /**
         * Regex for number, start with 0, and containing 10 character
         */
        if (phone.isEmpty()) {
            errors.put("phone", "Số điện thoại không được để trống");
        }
        else if (!phone.matches("^0\\d{9}$")) {
            errors.put("phone", "Số điện thoại không hợp lệ");
        }

        if(address1.isEmpty()){
            errors.put("address1", "Địa chỉ không được để trống");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);

            UserProfileDTO profile = userService.getUserProfile(userId);
            req.setAttribute("userProfile", profile);

            req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
            return;
        }

        // 2. Upload avatar (có thể null nếu không chọn ảnh)
        String imagePath = imageService.upload(req, "avatar");

        // 3. Update profile
        UserProfileDTO profile = userService.updateUserProfile(
                userId, fullName, phone, address1, address2, address3, imagePath
        );

        if (profile != null) {
            req.setAttribute("userProfile", profile);
            req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }
}
