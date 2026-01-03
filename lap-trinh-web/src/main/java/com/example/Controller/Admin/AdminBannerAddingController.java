package com.example.Controller.Admin;

import com.example.DAO.BannerDAO;
import com.example.Service.Admin.BannerService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Image.ImageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/admin/banners/adding")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB per file
        maxRequestSize = 1024 * 1024 * 50    // 50MB total
)
public class AdminBannerAddingController extends HttpServlet {
    private ImageService imageService;
    private BannerService bannerService;

    @Override
    public void init() throws ServletException {
        try {
            // Lấy connection trực tiếp từ JDBCConnection
            imageService = new ImageService();
            Connection conn = JDBCConnection.getConnection();
            bannerService = new BannerService(new BannerDAO(conn));
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/admin/pages/banner/banner-adding.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String thumbnailUrl = imageService.upload(req, "thumbnailFile");

        this.bannerService.addBanner(thumbnailUrl);

        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }
}
