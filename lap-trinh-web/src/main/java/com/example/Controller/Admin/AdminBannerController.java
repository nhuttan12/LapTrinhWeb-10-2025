package com.example.Controller.Admin;

import com.example.DTO.Banners.GetBannerImagesPagingResponseDTO;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Users.UserProfileDTO;
import com.example.Service.Admin.BannerService;
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
import java.util.Optional;

@WebServlet("/admin/banners")
public class AdminBannerController extends HttpServlet{
    private BannerService bannerService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            // Lấy connection trực tiếp từ JDBCConnection
            Connection conn = JDBCConnection.getConnection();
            bannerService = new BannerService(new com.example.DAO.BannerDAO(conn));
            userService = new UserService();
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int page = Optional.ofNullable(req.getParameter("page"))
                    .map(Integer::parseInt)
                    .orElse(1);

            int pageSize = Optional.ofNullable(req.getParameter("pageSize"))
                    .map(Integer::parseInt)
                    .orElse(10);

            HttpSession session = req.getSession(false);
            Integer userId = (Integer) session.getAttribute("userId");

            UserProfileDTO profile = userService.getUserProfile(userId);

            PagingResponse<GetBannerImagesPagingResponseDTO> banners = bannerService.getBannersPaging(page, pageSize);
            System.out.println("Logging banner: "+banners);

            if (profile != null) {
                req.setAttribute("userProfile", profile);
            }

            req.setAttribute("banners", banners.getItems());
            req.setAttribute("meta", banners.getMeta());
            req.setAttribute("activeMenu", "banners");
            req.getRequestDispatcher("/admin/pages/banner/banner-list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int thumbnailID = Integer.parseInt(req.getParameter("id"));

        this.bannerService.deleteBanner(thumbnailID);

        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }
}
