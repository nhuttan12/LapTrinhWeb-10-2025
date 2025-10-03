package com.example.Controller.Auth;

import com.example.Service.Auth.AuthService;
import com.example.Service.Auth.MailService;
import com.example.Service.Database.JDBCConnection;
import jakarta.mail.MessagingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/register")
public class Register extends HttpServlet {

    private AuthService authService;
    private MailService mailService;

    @Override
    public void init() throws ServletException {
        /*
         * init service
         */
        try {
            Connection conn = JDBCConnection.getConnection();
            authService = new AuthService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
        mailService = new MailService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/admin/pages/samples/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");

        System.out.println("Register attempt: " + username + ", " + email + ", " + password);

        /*
         * Validate
         */
        if (username == null || username.trim().length() < 4 ||
                password == null || password.trim().length() < 4 ||
                email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") ||
                !password.equals(retypePassword)) {

            req.setAttribute("errorMessage", "Thông tin không hợp lệ, vui lòng thử lại.");
            req.getRequestDispatcher("/admin/pages/samples/register.jsp").forward(req, resp);
            return;
        }

        boolean success = authService.signUp(username, email, password);

        if (success) {
            /*
             * Send confirmation email
             */
            String subject = "Đăng ký thành công tại website của chúng tôi!";
            String message = "<h2>Xin chào " + username + "!</h2>"
                    + "<p>Cảm ơn bạn đã đăng ký. Tài khoản của bạn đã được tạo thành công.</p>";

            try {
                /*
                 * Use HTML content
                 */
                mailService.sendEmail(email, subject, message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            /*
             * Redirect to /login page after successful signup
             */
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            /*
             * Set error message & forward back to /admin/pages/samples/register.jsp
             */
            req.setAttribute("errorMessage", "Đăng ký thất bại. Tên đăng nhập hoặc email đã tồn tại.");
            req.getRequestDispatcher("/admin/pages/samples/register.jsp").forward(req, resp);
        }
    }


}