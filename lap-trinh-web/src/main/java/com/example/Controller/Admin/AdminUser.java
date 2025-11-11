package com.example.Controller.Admin;

import com.example.DAO.AdminUserDAO;
import com.example.Model.RoleName;
import com.example.Model.User;
import com.example.Service.Database.JDBCConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUser extends HttpServlet {
    private AdminUserDAO adminUserDAO;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            adminUserDAO = new AdminUserDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize AdminUserServlet: " + e.getMessage());
        }
    }

    // Hiển thị danh sách user
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<User> users = null;
        try {
            users = adminUserDAO.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // --- Debug info ---
        if (users != null) {
            System.out.println("==== DEBUG: Số lượng user từ DB = " + users.size());
            for (User u : users) {
                System.out.println("ID=" + u.getId() +
                        ", Username=" + u.getUsername() +
                        ", Email=" + u.getEmail() +
                        ", Status=" + u.getStatus() +
                        ", RoleId=" + u.getRoleId());
            }
        } else {
            System.out.println("==== DEBUG: users == null");
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/admin/pages/UserManagement/Usermanagement.jsp")
                .forward(req, resp);
    }


    // Xử lý soft delete và change role
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String userIdStr = req.getParameter("userId");
        String message = "Invalid action";

        if (action == null || action.isBlank() || userIdStr == null || userIdStr.isBlank()) {
            req.getSession().setAttribute("message", "Action and userId are required");
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("message", "Invalid user ID");
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        try {
            switch (action) {
                case "softDelete" -> {
                    boolean success = adminUserDAO.toggleUserStatus(userId);
                    message = success ? "User soft deleted" : "User not found";
                }
                case "changeRole" -> {
                    String newRoleNameStr = req.getParameter("newRoleName");
                    if (newRoleNameStr == null || newRoleNameStr.isBlank()) {
                        message = "newRoleName is required";
                        break;
                    }

                    RoleName roleName;
                    try {
                        roleName = RoleName.fromString(newRoleNameStr);
                        boolean success = adminUserDAO.changeUserRole(userId, roleName);
                        message = success ? "User role updated" : "Failed to update role";
                    } catch (IllegalArgumentException e) {
                        message = "Invalid role name";
                    }
                }
                default -> message = "Unknown action";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Server error: " + e.getMessage();
        }

        req.getSession().setAttribute("message", message);
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
