package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.RegistrationDto;
import org.example.models.Faculty;
import org.example.service.AuthService;
import org.example.service.CityService;
import org.example.service.FacultyService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private AuthService authService;
    private FacultyService facultyService;
    private CityService cityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.authService = (AuthService) config.getServletContext().getAttribute("authService");
        this.facultyService = (FacultyService) config.getServletContext().getAttribute("facultyService");
        this.cityService = (CityService) config.getServletContext().getAttribute("cityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("faculties", facultyService.getAllFaculties());
        req.setAttribute("cities", cityService.getAllCities());
        req.getRequestDispatcher("/jsp/registration-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .fullName(req.getParameter("fullName"))
                .cityId(Integer.valueOf(req.getParameter("cityId")))
                .groupId(Integer.valueOf(req.getParameter("groupId")))
                .build();
        try {
            authService.register(registrationDto);
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("faculties", facultyService.getAllFaculties());
            req.setAttribute("cities", cityService.getAllCities());
            req.getRequestDispatcher("/jsp/registration-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Внутренняя ошибка базы данных", e);
        }
    }
}
