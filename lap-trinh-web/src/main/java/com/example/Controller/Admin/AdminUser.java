package com.example.Controller.Admin;
import com.example.Controller.DAO.UserDAO;
import com.example.Model.User;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUser extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            userDAO = new UserDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize AdminUser servlet: " + e.getMessage());
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách user
        List<User> users = userDAO.getAllUsers();

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Tạm thời viết JSON thủ công, bạn có thể dùng Gson nếu muốn
        out.print("[");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            out.print("{");
            out.printf("\"id\":%d,", u.getId());
            out.printf("\"username\":\"%s\",", u.getUsername());
            out.printf("\"email\":\"%s\",", u.getEmail());
            out.printf("\"roleId\":%d,", u.getRoleId());
            out.printf("\"status\":\"%s\"", u.getStatus().toString());
            out.print("}");
            if (i < users.size() - 1) out.print(",");
        }
        out.print("]");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int userId = Integer.parseInt(req.getParameter("userId"));
        String message = "Invalid action";

        switch (action) {
            case "softDelete" -> {
                boolean success = userDAO.softDeleteUser(userId);
                message = success ? "User soft deleted" : "User not found";
            }
            case "changeRole" -> {
                int newRoleId = Integer.parseInt(req.getParameter("newRoleId"));
                boolean success = userDAO.changeUserRole(userId, newRoleId);
                message = success ? "Role updated" : "Update failed";
            }
        }

        resp.setContentType("text/plain");
        resp.getWriter().write(message);
    }
}
