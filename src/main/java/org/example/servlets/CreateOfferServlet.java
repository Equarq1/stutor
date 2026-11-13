package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.dto.CreateFileInfoDto;
import org.example.dto.CreateOfferDto;
import org.example.models.User;
import org.example.models.enums.OfferFormat;
import org.example.service.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/create-offer")
@MultipartConfig
public class CreateOfferServlet extends HttpServlet {

    private OfferService offerService;
    private CategoryService categoryService;
    private SubjectService subjectService;
    private FacultyService facultyService;
    private FileInfoService fileInfoService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
        categoryService = (CategoryService) config.getServletContext().getAttribute("categoryService");
        subjectService = (SubjectService) config.getServletContext().getAttribute("subjectService");
        facultyService = (FacultyService) config.getServletContext().getAttribute("facultyService");
        fileInfoService = (FileInfoService) config.getServletContext().getAttribute("fileInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("offerFormats", OfferFormat.values());
        req.setAttribute("categories", categoryService.getAllCategories());
        req.setAttribute("faculties", facultyService.getAllFaculties());
        req.getRequestDispatcher("/jsp/create-offer-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User currentUser = (User) req.getSession().getAttribute("user");
        String[] subjectIdsRaw = req.getParameterValues("subjectIds");
        List<Integer> subjectIds = (subjectIdsRaw != null)
                ? Arrays.stream(subjectIdsRaw).map(Integer::parseInt).toList()
                : List.of();


        Part filePart = req.getPart("file");
        Long attachmentId = null;

        if (filePart != null && filePart.getSize() > 0 && !filePart.getSubmittedFileName().isEmpty()) {
            CreateFileInfoDto fileDto = CreateFileInfoDto.builder()
                    .originalFileName(filePart.getSubmittedFileName())
                    .size(filePart.getSize())
                    .type(filePart.getContentType())
                    .build();
            try {
                attachmentId = fileInfoService.save(filePart.getInputStream(), fileDto);
                resp.sendRedirect(req.getContextPath() + "/home");
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при сохранении прикрепленного файла", e);
            }
        }


        CreateOfferDto createOfferDto = CreateOfferDto.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .price(new BigDecimal(req.getParameter("price").replace(",", ".")))
                .format(OfferFormat.valueOf(req.getParameter("format")))
                .authorId(currentUser.getId())
                .categoryId(Integer.valueOf(req.getParameter("categoryId")))
                .subjectIds(subjectIds)
                .attachmentId(attachmentId)
                .build();

        try {
            offerService.save(createOfferDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
