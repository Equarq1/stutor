package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.User;
import org.example.models.enums.UserStatus;
import org.example.service.UserService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users", userService.getAllUsers());
        req.getRequestDispatcher("/jsp/admin-users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentAdmin = (User) req.getSession().getAttribute("user");

        Long userId;
        UserStatus newStatus;
        try {
            userId = Long.parseLong(req.getParameter("userId"));
            newStatus = UserStatus.valueOf(req.getParameter("status"));
        } catch (IllegalArgumentException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные параметры");
            return;
        }

        if (Objects.equals(currentAdmin.getId(), userId)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Нельзя изменить статус самому себе");
            return;
        }

        userService.changeStatus(userId, newStatus);
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}