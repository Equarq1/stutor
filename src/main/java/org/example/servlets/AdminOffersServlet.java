package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.OfferService;

import java.io.IOException;

@WebServlet("/admin/offers")
public class AdminOffersServlet extends HttpServlet {

    private OfferService offerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("offers", offerService.getAllOffersForAdmin());
        req.getRequestDispatcher("/jsp/admin-offers.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long offerId;
        try {
            offerId = Long.parseLong(req.getParameter("offerId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID объявления");
            return;
        }

        offerService.deleteById(offerId);
        resp.sendRedirect(req.getContextPath() + "/admin/offers");
    }
}