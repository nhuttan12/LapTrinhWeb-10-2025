package com.example.Controller.User.Cart;

import com.example.Model.Cart;
import com.example.Model.CartDetail;
import com.example.Service.Cart.CartService;
import com.example.Service.Database.JDBCConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            cartService = new CartService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize CartService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        String productIdParam = req.getParameter("productId");

        // ========================
        //   XÓA SẢN PHẨM (GET)
        // ========================
        if ("remove".equalsIgnoreCase(action) && productIdParam != null) {
            int productId = Integer.parseInt(productIdParam);

            Cart cart = cartService.getCartByUserId(userId);
            if (cart != null) {
                try {
                    cartService.removeProductFromCart(cart.getId(), productId);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // ========================
        //   LOAD CART PAGE
        // ========================
        Cart cart = cartService.getCartByUserId(userId);
        req.setAttribute("cart", cart);

        int cartQuantity = 0;

        if (cart != null) {
            List<CartDetail> details = cartService.getCartDetails(cart.getId());
            req.setAttribute("cartDetails", details);

            for (CartDetail d : details) {
                cartQuantity += d.getQuantity();
            }
        }

        req.getSession().setAttribute("cartQuantity", cartQuantity);

        req.getRequestDispatcher("/user/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {

            // =====================================
            //  ADD TO CART
            // =====================================
            if ("add".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(req.getParameter("productId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));

                cartService.addProductToCart(userId, productId, quantity);
            }

            // =====================================
            //  REMOVE (POST)
            // =====================================
            else if ("remove".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(req.getParameter("productId"));

                Cart cart = cartService.getCartByUserId(userId);
                if (cart != null) {
                    cartService.removeProductFromCart(cart.getId(), productId);
                }
            }

            // =====================================
            //  UPDATE QUANTITY
            // =====================================
            else if ("update".equalsIgnoreCase(action)) {

                Cart cart = cartService.getCartByUserId(userId);
                if (cart == null) {
                    cart = cartService.createCartForUser(userId);
                }

                List<CartDetail> details = cartService.getCartDetails(cart.getId());

                for (CartDetail d : details) {
                    String param = "quantity_" + d.getProductId();
                    String value = req.getParameter(param);

                    if (value != null && !value.isEmpty()) {
                        int quantity = Integer.parseInt(value);

                        if (quantity <= 0) {
                            cartService.removeProductFromCart(cart.getId(), d.getProductId());
                        } else {
                            cartService.updateQuantity(cart.getId(), d.getProductId(), quantity);
                        }
                    }
                }
            }

            // ========================
            // UPDATE cartQuantity
            // ========================
            Cart updated = cartService.getCartByUserId(userId);
            int cartQuantity = 0;

            if (updated != null) {
                for (CartDetail d : cartService.getCartDetails(updated.getId())) {
                    cartQuantity += d.getQuantity();
                }
            }

            req.getSession().setAttribute("cartQuantity", cartQuantity);
            resp.sendRedirect(req.getContextPath() + "/cart");

        } catch (Exception e) {
            throw new ServletException("Error updating cart", e);
        }
    }
}
