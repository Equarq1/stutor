package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.CityService;

import java.io.IOException;

@WebServlet("/admin/cities")
public class AdminCitiesServlet extends HttpServlet {

    private CityService cityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        cityService = (CityService) config.getServletContext().getAttribute("cityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                cityService.addCity(req.getParameter("name"));
            } else if ("delete".equals(action)) {
                cityService.deleteCity(Integer.parseInt(req.getParameter("cityId")));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неизвестное действие");
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID города");
            return;
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showPage(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/cities");
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cities", cityService.getAllCities());
        req.getRequestDispatcher("/jsp/admin-cities.jsp").forward(req, resp);
    }
}