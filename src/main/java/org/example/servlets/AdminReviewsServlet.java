package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.ReviewService;

import java.io.IOException;

@WebServlet("/admin/reviews")
public class AdminReviewsServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        reviewService = (ReviewService) config.getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("reviews", reviewService.getAllReviews());
        req.getRequestDispatcher("/jsp/admin-reviews.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long reviewId;
        try {
            reviewId = Long.parseLong(req.getParameter("reviewId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID отзыва");
            return;
        }

        reviewService.deleteReview(reviewId);
        resp.sendRedirect(req.getContextPath() + "/admin/reviews");
    }
}