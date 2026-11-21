package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.User;
import org.example.service.ReviewService;

import java.io.IOException;

@WebServlet("/profile")
public class MyProfileServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        reviewService = (ReviewService) config.getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        req.setAttribute("user", currentUser);
        req.setAttribute("reviews", reviewService.getReviewsByTutorId(currentUser.getId()));
        req.setAttribute("avgRating", reviewService.getAverageRatingByTutorId(currentUser.getId()));
        req.getRequestDispatcher("/jsp/my-profile-page.jsp").forward(req, resp);
    }
}