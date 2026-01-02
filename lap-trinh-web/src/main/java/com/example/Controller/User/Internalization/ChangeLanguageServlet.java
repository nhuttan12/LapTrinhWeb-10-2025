package com.example.Controller.User.Internalization;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@WebServlet("/change-language")
public class ChangeLanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String lang = request.getParameter("lang");

        HttpSession session = request.getSession();

        if (lang != null) {
            /**
             * Save locale in session
             */
            switch (lang) {
                case "vi":
                    session.setAttribute("language", "vi_VN");
                    break;
                case "en":
                default:
                    session.setAttribute("language", "en_US");
                    break;
            }
        }
        /**
         * Redirect back to where user came from
         */
        String referer = request.getHeader("Referer");
        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}