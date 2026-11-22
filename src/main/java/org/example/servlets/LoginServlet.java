package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.LoginDto;
import org.example.models.User;
import org.example.models.enums.UserStatus;
import org.example.service.AuthService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init(ServletConfig config) throws ServletException {
         authService = (AuthService) config.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("true".equals(req.getParameter("blocked"))) {
            req.setAttribute("errorMessage", "Ваш аккаунт заблокирован");
        }
        req.getRequestDispatcher("/jsp/login-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.authenticate(
                    LoginDto.builder()
                            .email(req.getParameter("email"))
                            .password(req.getParameter("password"))
                            .build()
            );



            if (user.getUserStatus().equals(UserStatus.BLOCKED)) {
                throw new IllegalArgumentException("Ваш аккаунт заблокирован");
            }

            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/jsp/login-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка бд", e);
        }
    }
}
