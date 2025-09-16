package com.example.Controller.ThankYou;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/thankyou")
public class ThankYou extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // forward sang file JSP
        req.getRequestDispatcher("/user/thankyou.jsp").forward(req, resp);
    }
}