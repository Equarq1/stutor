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

@WebServlet("/offer")
public class OfferServlet extends HttpServlet {
    private OfferService offerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/offers");
            return;
        }

        try {
            Offer offer = offerService.getOfferById(Integer.parseInt(idParam));
            if (offer == null ) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Объявление не найдено");
                return;
            }

            req.setAttribute("offer", offer);
            req.getRequestDispatcher("/jsp/offer-page.jsp").forward(req, resp);
        }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/offers");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
