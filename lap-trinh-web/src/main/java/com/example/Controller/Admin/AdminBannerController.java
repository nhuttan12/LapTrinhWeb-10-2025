package com.example.Controller.Admin;
import com.example.Model.Image;
import com.example.Service.Admin.BannerService;
import com.example.Service.Database.JDBCConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
@WebServlet("/admin/banners")
public class AdminBannerController extends HttpServlet{
    private BannerService bannerService;

    @Override
    public void init() throws ServletException {
        try {
            // Lấy connection trực tiếp từ JDBCConnection
            Connection conn = JDBCConnection.getConnection();
            bannerService = new BannerService(new com.example.DAO.BannerDAO(conn));
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Image> banners = bannerService.getAllBanners();
            req.setAttribute("banners", banners);
            req.getRequestDispatcher("/admin/pages/banner/banner-list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                String url = req.getParameter("url");
                bannerService.addBanner(url);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                bannerService.deleteBanner(id);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/banners");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
