package com.example.Controller.Auth;

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
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends HttpServlet {

    private AuthService authService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        /*
         * init service
         */
        try {
            Connection conn = JDBCConnection.getConnection();
            authService = new AuthService(conn);
            userService = new UserService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         * forward sang file JSP
         */
        req.getRequestDispatcher("/admin/pages/samples/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean success = authService.login(username, password);

        /*
         * Validate
         */
        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Tài khoản và mật khẩu không được để trống!");
            req.getRequestDispatcher("/admin/pages/samples/login.jsp").forward(req, resp);
            return;
        }

        if (username.length() < 4 || password.length() < 4) {
            req.setAttribute("errorMessage", "Tài khoản và mật khẩu phải có ít nhất 4 ký tự!");
            req.getRequestDispatcher("/admin/pages/samples/login.jsp").forward(req, resp);
            return;
        }

        if (success) {
            HttpSession session = req.getSession();

            User user = userService.getUserByUsername(username);

            session.setAttribute("username", username);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user);

            /*
             * redirect to /home
             */
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            /*
             * set error message and forward back to /admin/pages/samples/login.jsp
             */
            req.setAttribute("errorMessage", "Tài khoản và mật khẩu không hợp lệ");
            req.getRequestDispatcher("/admin/pages/samples/login.jsp").forward(req, resp);
        }
    }
}