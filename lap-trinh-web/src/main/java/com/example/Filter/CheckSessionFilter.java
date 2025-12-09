package com.example.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class CheckSessionFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/",
            "/login",
            "/product-list",
            "/about",
            "/contact",
            "/register",
            "/home",
            "/product-filter",
            "/product-detail",
            "/change-language",
            "/payment/vnpay-return",
            "/payment/vnpay-ipn"
    );

    private static final List<String> VNPAY_PATHS = Arrays.asList(
            "/payment/vnpay-return",
            "/payment/vnpay-ipn"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        /*
         * Create request and response and get session from request
         */
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        /*
         * Get path from request
         */
        String path = req.getRequestURI().substring(req.getContextPath().length());

        /*
         * Check if path is public or user is logged in
         */
        String token = req.getParameter("token");
        boolean isPublic = PUBLIC_PATHS.contains(path);
        boolean isVnPay = VNPAY_PATHS.contains(path);
        boolean loggedIn = (
                session != null &&
                        session.getAttribute("username") != null &&
                        session.getAttribute("userId") != null);

        /**
         * Check resource file
         */
        boolean isStaticResource =
                path.startsWith("/admin/") ||
                        path.startsWith("/vendors/") ||
                        path.startsWith("/images/") ||
                        path.startsWith("/css/") ||
                        path.startsWith("/js/") ||
                        path.endsWith(".css") ||
                        path.endsWith(".js") ||
                        path.endsWith(".png") ||
                        path.endsWith(".jpg") ||
                        path.endsWith(".jpeg") ||
                        path.endsWith(".gif") ||
                        path.endsWith(".svg") ||
                        path.endsWith(".ico") ||
                        path.endsWith(".woff") ||
                        path.endsWith(".woff2") ||
                        path.endsWith(".ttf");

        if (isPublic || loggedIn || isStaticResource ||token != null) {
            /*
             * Allow request to continue
             */
            chain.doFilter(request, response);
        } else {
            /*
             * Redirect to login page
             */
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}

