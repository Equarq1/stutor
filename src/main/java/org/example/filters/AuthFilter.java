package org.example.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.models.User;
import org.example.models.enums.UserStatus;
import org.example.service.UserService;

import java.io.IOException;

@WebFilter({
        "/login", "/registration",
        "/profile", "/user", "/create-offer", "/offers", "/my-offers", "/offer",
        "/offer/update", "/offer/delete", "/offer/status", "/profile/update",
        "/requests", "/request/create", "/request/status", "/review/create", "/file"
})
public class AuthFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = (UserService) filterConfig.getServletContext().getAttribute("userService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {



        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String contextPath = req.getContextPath();
        String path = req.getServletPath();
        boolean isAuthPage = path.equals("/login") || path.equals("/registration");

        HttpSession session = req.getSession(false);
        boolean isAuthenticated = (session != null && session.getAttribute("user") != null);

        if (isAuthenticated) {
            User sessionUser = (User) session.getAttribute("user");
            User currentUser = userService.getUserById(sessionUser.getId());
            if (currentUser == null || currentUser.getUserStatus() == UserStatus.BLOCKED) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/login?blocked=true");
                return;
            }
        }

        if ((isAuthenticated && !isAuthPage) || (!isAuthenticated && isAuthPage)) {
            filterChain.doFilter(req, resp);
        } else if (isAuthenticated && isAuthPage) {
            resp.sendRedirect(contextPath + "/home");
        } else {
            resp.sendRedirect(contextPath + "/login");
        }
    }
}