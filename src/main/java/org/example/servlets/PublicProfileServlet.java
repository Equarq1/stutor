package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.User;
import org.example.service.ReviewService;
import org.example.service.UserService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/user")
public class PublicProfileServlet extends HttpServlet {

    private UserService userService;
    private ReviewService reviewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute("userService");
        reviewService = (ReviewService) config.getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");



        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Long userId;
        try {
            userId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        User targetUser = userService.getUserById(userId);
        User currentUser = (User) req.getSession().getAttribute("user");
        if (targetUser == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
            return;
        }

        if (currentUser != null && Objects.equals(currentUser.getId(), targetUser.getId())) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }



        req.setAttribute("targetUser", targetUser);
        req.setAttribute("reviews", reviewService.getReviewsByTutorId(targetUser.getId()));
        req.setAttribute("avgRating", reviewService.getAverageRatingByTutorId(targetUser.getId()));
        req.getRequestDispatcher("/jsp/profile-page.jsp").forward(req, resp);
    }
}
