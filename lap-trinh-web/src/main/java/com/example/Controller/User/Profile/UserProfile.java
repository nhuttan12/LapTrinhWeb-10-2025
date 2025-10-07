package com.example.Controller.User.Profile;

import com.example.Model.User;
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

@WebServlet("/profile")
public class UserProfile extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            userService = new UserService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");

        User user = userService.getUserProfile(userId);

        if (user != null) {
            req.setAttribute("user", user);
            req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }
}
