package com.example.Controller.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin")
public class Admin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy trang trước đó
        String referer = req.getHeader("Referer");

        // Chỉ lưu khi đi từ USER -> ADMIN (không ghi đè khi đang ở admin)
        if (referer != null && !referer.contains("/admin")) {
            req.getSession().setAttribute("ADMIN_BACK_URL", referer);
        }

        // forward sang file JSP
        req.setAttribute("activeMenu", "products");
        req.getRequestDispatcher("/admin/pages/productManagement/product-list.jsp").forward(req, resp);
    }
}