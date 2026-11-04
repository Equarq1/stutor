package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.StudyGroup;
import org.example.service.StudyGroupService;

import java.io.IOException;
import java.time.Year;
import java.util.List;

@WebServlet("/api/groups")
public class GroupApiServlet extends HttpServlet {
    private StudyGroupService studyGroupService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.studyGroupService = (StudyGroupService) config.getServletContext().getAttribute("studyGroupService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int facultyId = Integer.parseInt(req.getParameter("facultyId"));
        int course = Integer.parseInt(req.getParameter("course"));
        int currentYear = Year.now().getValue();
        int admissionYear = currentYear - course + 1;

        List<StudyGroup> studyGroups = studyGroupService.getStudyGroupsByFacultyAndYear(facultyId, admissionYear);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String groupsAsJson = objectMapper.writeValueAsString(studyGroups);
        resp.getWriter().println(groupsAsJson);

    }

}
