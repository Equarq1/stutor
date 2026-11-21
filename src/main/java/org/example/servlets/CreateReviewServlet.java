package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CreateReviewDto;
import org.example.models.Request;
import org.example.models.User;
import org.example.models.enums.RequestStatus;
import org.example.service.RequestService;
import org.example.service.ReviewService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/review/create")
public class CreateReviewServlet extends HttpServlet {

    private ReviewService reviewService;
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        reviewService = (ReviewService) config.getServletContext().getAttribute("reviewService");
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long requestId;
        try {
            requestId = Long.parseLong(req.getParameter("requestId"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/requests");
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("user");
        Request targetRequest = requestService.getRequestById(requestId).orElse(null);

        if (targetRequest == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Заявка не найдена");
            return;
        }

        if (!Objects.equals(targetRequest.getStudent().getId(), currentUser.getId())
                || targetRequest.getStatus() != RequestStatus.ACCEPTED) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нельзя оставить отзыв по этой заявке");
            return;
        }

        if (reviewService.existsByRequestId(requestId)) {
            resp.sendRedirect(req.getContextPath() + "/user?id=" + targetRequest.getOffer().getAuthor().getId());
            return;
        }

        req.setAttribute("targetRequest", targetRequest);
        req.getRequestDispatcher("/jsp/create-review-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("user");

        Long requestId;
        Short rating;
        try {
            requestId = Long.parseLong(req.getParameter("requestId"));
            rating = Short.parseShort(req.getParameter("rating"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные параметры");
            return;
        }

        String comment = req.getParameter("comment");

        CreateReviewDto dto = CreateReviewDto.builder()
                .requestId(requestId)
                .studentId(currentUser.getId())
                .rating(rating)
                .comment(comment)
                .build();

        try {
            Long tutorId = reviewService.createReview(dto);
            resp.sendRedirect(req.getContextPath() + "/user?id=" + tutorId);
        } catch (IllegalArgumentException e) {
            Request targetRequest = requestService.getRequestById(requestId).orElse(null);
            req.setAttribute("targetRequest", targetRequest);
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/jsp/create-review-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении отзыва", e);
        }
    }
}