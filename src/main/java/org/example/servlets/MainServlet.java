package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Offer;
import org.example.service.OfferService;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class MainServlet extends HttpServlet {
    private OfferService offerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Offer> offers = offerService.getLastOffersWithLimit(3);
        req.setAttribute("lastOffers", offers);
        req.getRequestDispatcher("/jsp/main-page.jsp").forward(req, resp);
    }

}
