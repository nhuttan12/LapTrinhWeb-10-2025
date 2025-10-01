package com.example.Controller.Auth;

import com.example.Service.Auth.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        /*
         * init service
         */
        authService = new AuthService();
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
            session.setAttribute("username", username);

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