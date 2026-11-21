package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Request;
import org.example.models.User;
import org.example.models.enums.RequestStatus;
import org.example.service.RequestService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/request/status")
public class RequestStatusServlet extends HttpServlet {
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long requestId = Long.parseLong(req.getParameter("requestId"));
        RequestStatus status = RequestStatus.valueOf(req.getParameter("status"));
        User currentUser = (User) req.getSession().getAttribute("user");

        Request request = requestService.getRequestById(requestId).orElse(null);
        if (request == null || !Objects.equals(currentUser.getId(), request.getOffer().getAuthor().getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на изменение статуса заявки");
            return;
        }
        requestService.changeStatus(requestId, status);
        resp.sendRedirect(req.getContextPath() + "/requests?type=incoming");
    }
}