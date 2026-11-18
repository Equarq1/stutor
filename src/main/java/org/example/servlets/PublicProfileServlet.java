package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.User;
import org.example.service.UserService;

import java.io.IOException;

@WebServlet("/user") // Не придумал надо поменять
public class PublicProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        User targetUser = userService.getUserById(Long.parseLong(idParam));
        if (targetUser == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
            return;
        }

        req.setAttribute("targetUser", targetUser);
        req.getRequestDispatcher("/jsp/profile-page.jsp").forward(req, resp);
    }
}
