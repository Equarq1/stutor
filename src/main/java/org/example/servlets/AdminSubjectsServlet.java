package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.FacultyService;
import org.example.service.SubjectService;

import java.io.IOException;

@WebServlet("/admin/subjects")
public class AdminSubjectsServlet extends HttpServlet {

    private SubjectService subjectService;
    private FacultyService facultyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        subjectService = (SubjectService) config.getServletContext().getAttribute("subjectService");
        facultyService = (FacultyService) config.getServletContext().getAttribute("facultyService");
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
                String name = req.getParameter("name");
                int facultyId = Integer.parseInt(req.getParameter("facultyId"));
                int course = Integer.parseInt(req.getParameter("course"));
                subjectService.addSubject(name, course, facultyId);
            } else if ("delete".equals(action)) {
                subjectService.deleteSubject(Integer.parseInt(req.getParameter("subjectId")));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неизвестное действие");
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные параметры");
            return;
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showPage(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/subjects");
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("subjects", subjectService.getAllSubjects());
        req.setAttribute("faculties", facultyService.getAllFaculties());
        req.getRequestDispatcher("/jsp/admin-subjects.jsp").forward(req, resp);
    }
}