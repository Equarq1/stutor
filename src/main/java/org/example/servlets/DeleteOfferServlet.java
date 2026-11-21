package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Offer;
import org.example.models.User;
import org.example.service.OfferService;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/offer/delete")
public class DeleteOfferServlet extends HttpServlet {

    private OfferService offerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Offer offer = offerService.getOfferById(id);
        User currentUser = (User) req.getSession().getAttribute("user");


        if (offer == null || !Objects.equals(currentUser.getId(), offer.getAuthor().getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на удаление");
            return;
        }

        offerService.deleteById(id);
        resp.sendRedirect(req.getContextPath() + "/my-offers");
    }
}
