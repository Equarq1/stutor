package org.example.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({"/profile", "/create-offer", "/offers", "/my-offers", "/offer"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession(false);
        boolean isLogged = (session != null && session.getAttribute("user") != null);

        if (isLogged) {
            filterChain.doFilter(req, resp);
        }
        else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }


    }
}
