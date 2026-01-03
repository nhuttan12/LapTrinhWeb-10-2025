package com.example.Controller.User.Payment;

import com.example.Model.*;
import com.example.Service.Cart.CartService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderServiceSimple;
import com.example.Service.Payment.PaymentService;
import com.example.Service.Payment.PaypalCreateResult;
import com.example.Service.Payment.PaypalService;
import com.example.Service.Payment.VnPayService;
import com.example.Service.User.UserDetailService;
import com.example.Service.User.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/checkout/payment")
public class PaymentController extends HttpServlet {

    private Connection conn;
    private CartService cartService;
    private OrderServiceSimple orderService;
    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {
        try {
            conn = JDBCConnection.getConnection();
            cartService = new CartService(conn);
            orderService = new OrderServiceSimple(conn);
            paymentService = new PaymentService(conn);
        } catch (Exception e) {
            throw new ServletException("Failed to init services", e);
        }
    }

    @Override
    public void destroy() {
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Lấy userId
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2. Lấy thông tin khách hàng từ form
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String notes = req.getParameter("notes");
        String methodStr = req.getParameter("method");

        if(fullName == null || email == null || phone == null || address == null || methodStr == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số bắt buộc");
            return;
        }

        PaymentMethod method = PaymentMethod.fromString(methodStr);
        if (method == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Phương thức thanh toán không hợp lệ");
            return;
        }

        try {
            // --- Cập nhật thông tin user trước khi tạo order/payment ---
            UserService userService = new UserService(conn);
            userService.updateFullName(userId, fullName); // cần method trong UserService

            UserDetailService userDetailService = new UserDetailService(conn);
            userDetailService.update(userId, phone, address);

            // 3. Lấy Cart của user
            Cart cart = cartService.getCartByUserId(userId);
            if (cart == null || cart.getCartDetails().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            // 4. Tạo Order từ Cart
            int orderId = orderService.createOrderFromCart(cart);

            // 4. Xóa những sản phẩm đã mua khỏi giỏ (soft delete)
            cartService.removePurchasedItems(cart.getId(), cart.getCartDetails());

            // 4.1 Lấy lại cart mới
            Cart updatedCart = cartService.getCartByUserId(userId);

            // 4.2 Tính tổng số lượng mới
            int totalQuantity = 0;
            if (updatedCart != null && updatedCart.getCartDetails() != null) {
                for (var d : updatedCart.getCartDetails()) {
                    totalQuantity += d.getQuantity();
                }
            }

            // 4.3 Cập nhật session
            req.getSession().setAttribute("cart", updatedCart);
            req.getSession().setAttribute("cartQuantity", totalQuantity);

            // 5. Tạo Payment
            Payment payment = Payment.builder()
                    .orderId(orderId)
                    .amount(cart.getTotalPrice())
                    .method(method)
                    .status(PaymentStatus.PENDING)
                    .provider(method.getMethod())
                    .build();

            int paymentId = paymentService.createPayment(payment);

            if (method == PaymentMethod.VNPAY_TEST) {
                String returnUrl = "https://brantlee-decipherable-alexandra.ngrok-free.dev"
                        + req.getContextPath() + "/payment/vnpay-return";
                String ipAddress = req.getRemoteAddr();

                String paymentUrl = new VnPayService().generatePaymentUrl(
                        orderId,
                        (long)cart.getTotalPrice(),
                        returnUrl,
                        ipAddress
                );
                resp.sendRedirect(paymentUrl);
                return;
            }

            if (method == PaymentMethod.PAYPAL_TEST) {
                String returnUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                        + req.getContextPath() + "/payment/paypal-return";
                String cancelUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                        + req.getContextPath() + "/checkout/payment?cancel=1";

                PaypalService paypal = new PaypalService();
                PaypalCreateResult res = paypal.createOrder(orderId, cart.getTotalPrice(), returnUrl, cancelUrl);

                // lưu PayPal ORDER ID vào DB
                paymentService.updateTransactionId(paymentId, res.getPaypalOrderId());

                // redirect sang paypal
                resp.sendRedirect(res.getApprovalUrl());
                return;
            }

            // 6. Cập nhật trạng thái Payment + Order
            if (method == PaymentMethod.COD) {
                // COD: khách thanh toán khi nhận hàng
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.PENDING);
                orderService.updateShippingStatus(orderId, ShippingStatus.PENDING); // shipping bắt đầu pending
            } else {
                // Thanh toán online (PayPal, VNPAY,...)
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.COMPLETED);
                orderService.updateShippingStatus(orderId, ShippingStatus.PENDING); // hàng chưa gửi nhưng thanh toán xong
            }


            // 7. Chuyển hướng sang chi tiết đơn hàng
            resp.sendRedirect(req.getContextPath() + "/order-detail?orderId=" + orderId);

        } catch (Exception e) {
            throw new ServletException("Lỗi khi xử lý thanh toán", e);
        }
    }

}