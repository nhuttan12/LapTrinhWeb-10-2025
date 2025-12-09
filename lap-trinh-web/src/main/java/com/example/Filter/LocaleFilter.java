package com.example.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@WebFilter("/*")
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession session = req.getSession(false);

        /**
         * Default language
         */
        String language = "vi_VN";

        if (session != null) {
            Object langObj = session.getAttribute("language");
            if (langObj != null) {
                language = langObj.toString();
            }
        }

        /**
         * Convert string to locale
         */
        String[] parts = language.split("_");
        Locale locale = new Locale(parts[0], parts[1]);

        /**
         * Set locale for JSTL
         */
        request.setAttribute("javax.servlet.jsp.jstl.fmt.locale.request", locale);

        chain.doFilter(request, response);
    }
}