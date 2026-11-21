package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Offer;
import org.example.models.User;
import org.example.models.enums.OfferStatus;
import org.example.service.OfferService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/offer/status")
public class OfferStatusServlet extends HttpServlet {

    private OfferService offerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        OfferStatus status = OfferStatus.valueOf(req.getParameter("newStatus"));

        User currentUser = (User) req.getSession().getAttribute("user");
        Offer offer = offerService.getOfferById(id);

        if (offer == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Объявление не найдено");
            return;
        }

        if (!Objects.equals(currentUser.getId(), offer.getAuthor().getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на редактирование");
            return;
        }
        offerService.changeStatus(id, status);
        resp.sendRedirect(req.getContextPath() + "/my-offers");
    }
}
