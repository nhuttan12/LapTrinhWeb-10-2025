package com.example.Controller.User.Profile;

import com.example.DTO.Users.UserChangePasswordResponseDTO;
import com.example.Service.Auth.AuthService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.User.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {
    private UserService userService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            userService = new UserService(conn);
            authService = new AuthService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");

        UserChangePasswordResponseDTO user = userService.getUserChangePasswordResponse(userId);

        if (user != null) {
            req.setAttribute("username", user.getUsername());
            req.getRequestDispatcher("/user/change-password.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String newPassword = req.getParameter("newPassword");
        String retypePassword = req.getParameter("retypePassword");
        System.out.println("Info username: " + username + " password: " + password + " newPassword: " + newPassword + " retypePassword: " + retypePassword);

        if (newPassword == null || !newPassword.equals(retypePassword)) {
            req.setAttribute("error", "Nhập lại mật khẩu không khớp");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/user/change-password.jsp").forward(req, resp);
            return;
        }

        boolean success = authService.changePassword(username, password, newPassword);
        System.out.println("Update password in controller result: " + success);

        if (!success) {
            req.setAttribute("error", "Đổi mật khẩu thất bại");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/user/change-password.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("message", "Đổi mật khẩu thành công!");
        req.setAttribute("username", username);
        req.getRequestDispatcher("/user/change-password.jsp").forward(req, resp);
    }
}
