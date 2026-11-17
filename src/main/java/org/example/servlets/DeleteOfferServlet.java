package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.OfferService;

import java.io.IOException;

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
        offerService.deleteById(id);
        resp.sendRedirect(req.getContextPath() + "/my-offers");
    }
}
