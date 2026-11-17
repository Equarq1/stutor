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
import org.example.models.Offer;
import org.example.models.User;
import org.example.models.enums.OfferFormat;
import org.example.service.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/offer/update")
@MultipartConfig
public class UpdateOfferServlet extends HttpServlet {
    private OfferService offerService;
    private CategoryService categoryService;
    private FacultyService facultyService;
    private FileInfoService fileInfoService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
        categoryService = (CategoryService) config.getServletContext().getAttribute("categoryService");
        facultyService = (FacultyService) config.getServletContext().getAttribute("facultyService");
        fileInfoService = (FileInfoService) config.getServletContext().getAttribute("fileInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/my-offers");
            return;
        }

        Offer offer = offerService.getOfferById(Long.parseLong(idParam));
        User currentUser = (User) req.getSession().getAttribute("user");

        if (offer == null || !offer.getAuthor().getId().equals(currentUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на редактирование");
            return;
        }

        req.setAttribute("offer", offer);
        req.setAttribute("offerFormats", OfferFormat.values());
        req.setAttribute("categories", categoryService.getAllCategories());
        req.setAttribute("faculties", facultyService.getAllFaculties());

        req.getRequestDispatcher("/jsp/update-offer-page.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        Long offerId = Long.parseLong(req.getParameter("id"));

        Offer existingOffer = offerService.getOfferById(offerId);
        if (existingOffer == null || !existingOffer.getAuthor().getId().equals(currentUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на редактирование");
            return;
        }

        String[] subjectIdsRaw = req.getParameterValues("subjectIds");
        List<Integer> subjectIds = (subjectIdsRaw != null)
                ? Arrays.stream(subjectIdsRaw).map(Integer::parseInt).toList()
                : List.of();

        Long attachmentId = null;
        String oldAttachmentIdParam = req.getParameter("oldAttachmentId");
        if (oldAttachmentIdParam != null && !oldAttachmentIdParam.isEmpty()) {
            attachmentId = Long.parseLong(oldAttachmentIdParam);
        }

        Part filePart = req.getPart("file");
        if (filePart != null && filePart.getSize() > 0 && !filePart.getSubmittedFileName().isEmpty()) {
            CreateFileInfoDto fileDto = CreateFileInfoDto.builder()
                    .originalFileName(filePart.getSubmittedFileName())
                    .size(filePart.getSize())
                    .type(filePart.getContentType())
                    .build();
            try {
                attachmentId = fileInfoService.save(filePart.getInputStream(), fileDto);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка сохранения файла", e);
            }
        }

        CreateOfferDto dto = CreateOfferDto.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .price(new BigDecimal(req.getParameter("price").replace(",", ".")))
                .format(OfferFormat.valueOf(req.getParameter("format")))
                .categoryId(Integer.valueOf(req.getParameter("categoryId")))
                .subjectIds(subjectIds)
                .attachmentId(attachmentId)
                .build();

        try {
            offerService.update(offerId, dto);
            resp.sendRedirect(req.getContextPath() + "/offer?id=" + offerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}