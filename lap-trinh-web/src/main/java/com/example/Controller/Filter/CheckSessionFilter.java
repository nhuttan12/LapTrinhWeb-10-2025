package com.example.Controller.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter
public class CheckSessionFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login",
            "/product",
            "/about-us",
            "/contact",
            "/register"
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
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        boolean loggedIn = (session != null && session.getAttribute("username") != null);

        if (isPublic || loggedIn) {
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

