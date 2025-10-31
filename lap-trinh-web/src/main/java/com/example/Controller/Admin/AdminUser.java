package com.example.Controller.Admin;

import com.example.DAO.AdminUserDAO;
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

    /**
     * GET: Hiển thị danh sách user trên giao diện JSP
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<User> users = adminUserDAO.getAllUsers();

        req.setAttribute("users", users);
        req.getRequestDispatcher("/admin/page/UserManagement/UserManagement.jsp")
                .forward(req, resp);
    }

    /**
     * POST: Xử lý hành động (soft delete / change role)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        int userId = Integer.parseInt(req.getParameter("userId"));
        String message = "Invalid action";

        switch (action) {
            case "softDelete" -> {
                boolean success = adminUserDAO.softDeleteUser(userId);
                message = success ? "User soft deleted" : "User not found";
            }
            case "changeRole" -> {
                int newRoleId = Integer.parseInt(req.getParameter("newRoleId"));
                boolean success = adminUserDAO.changeUserRole(userId, newRoleId);
                message = success ? "User role updated" : "Failed to update role";
            }
        }

        // Lưu message hiển thị lại cho admin nếu cần
        req.getSession().setAttribute("message", message);

        // Quay lại danh sách user
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
