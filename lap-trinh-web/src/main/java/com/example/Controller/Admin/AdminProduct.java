package com.example.Controller.Admin;

import com.example.DAO.AdminProductDAO;
import com.example.Model.Product;
import com.example.Service.Admin.AdminProductService;
import com.example.Service.Database.JDBCConnection;
import com.example.DTO.Products.GetProductsPagingResponseDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/products")
public class AdminProduct extends HttpServlet {
    private AdminProductService productService;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            AdminProductDAO productDAO = new AdminProductDAO(conn);
            productService = new AdminProductService(productDAO);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize AdminProduct servlet: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "list":
                    handleList(req, resp);
                    break;
                case "detail":
                    handleDetail(req, resp);
                    break;
                case "delete":
                    handleDelete(req, resp);
                    break;
                default:
                    handleList(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void handleList(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int page = 1;
        int pageSize = 10;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }

        List<GetProductsPagingResponseDTO> products = productService.getProductsPaginated(page, pageSize);

        req.setAttribute("products", products);
        req.setAttribute("currentPage", page);
        req.getRequestDispatcher("/admin/pages/productManagement/product-list.jsp").forward(req, resp);
    }

    private void handleDetail(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?action=list");
            return;
        }

        int id = Integer.parseInt(idParam);
        Product product = productService.getProductById(id);

        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?action=list");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher("/admin/pages/productManagement/product-detail.jsp").forward(req, resp);
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productService.softDeleteProduct(id);
        resp.sendRedirect(req.getContextPath() + "/admin/products?action=list");
    }
}
