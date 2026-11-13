package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Subject;
import org.example.service.SubjectService;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/subjects")
public class SubjectApiServlet extends HttpServlet {
    private SubjectService subjectService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        subjectService = (SubjectService) config.getServletContext().getAttribute("subjectService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int facultyId = Integer.parseInt(req.getParameter("facultyId"));
        int course = Integer.parseInt(req.getParameter("course"));
        List<Subject> subjects = subjectService.getAllSubjectsByFacultyIdAndCourse(facultyId, course);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String subjectAsJson = objectMapper.writeValueAsString(subjects);
        resp.getWriter().println(subjectAsJson);
    }
}
