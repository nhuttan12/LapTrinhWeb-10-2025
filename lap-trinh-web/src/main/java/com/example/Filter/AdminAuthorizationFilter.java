package com.example.Filter;

import com.example.Model.RoleName;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminAuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/admin/css/") ||
                path.startsWith("/admin/js/") ||
                path.startsWith("/admin/images/") ||
                path.startsWith("/admin/vendors/") ||
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
                path.endsWith(".ttf")) {
            chain.doFilter(request, response);
            return;
        }


        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String roleName = (String) session.getAttribute("roleName");

        if (!RoleName.ADMIN.getRoleName().equals(roleName)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
