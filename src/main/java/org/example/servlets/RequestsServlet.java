package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.User;
import org.example.service.RequestService;

import java.io.IOException;

@WebServlet("/requests")
public class RequestsServlet extends HttpServlet {
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String type = req.getParameter("type");

        if ("incoming".equals(type)) {
            req.setAttribute("requests", requestService.getIncomingRequests(user.getId()));
            req.setAttribute("pageTitle", "Входящие заявки");
            req.setAttribute("isIncoming", true);
        } else {
            req.setAttribute("requests", requestService.getOutgoingRequests(user.getId()));
            req.setAttribute("pageTitle", "Мои отклики");
            req.setAttribute("isIncoming", false);
        }

        req.getRequestDispatcher("/jsp/requests-page.jsp").forward(req, resp);
    }
}