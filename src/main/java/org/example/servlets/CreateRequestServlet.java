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
import org.example.dto.CreateRequestDto;
import org.example.models.Offer;
import org.example.models.User;
import org.example.service.FileInfoService;
import org.example.service.OfferService;
import org.example.service.RequestService;
import java.lang.Integer;
import org.example.models.enums.OfferStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/request/create")
@MultipartConfig(maxFileSize = 10485760)
public class CreateRequestServlet extends HttpServlet {

    private OfferService offerService;
    private RequestService requestService;
    private FileInfoService fileInfoService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
        offerService = (OfferService) config.getServletContext().getAttribute("offerService");
        fileInfoService = (FileInfoService) config.getServletContext().getAttribute("fileInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long offerId;
        try {
            offerId = Long.parseLong(req.getParameter("offerId"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/offers");
            return;
        }

        Offer offer = offerService.getOfferById(offerId);
        User user = (User) req.getSession().getAttribute("user");

        if (offer == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Объявление не найдено");
            return;
        }

        if (offer.getStatus() != OfferStatus.ACTIVE || Objects.equals(offer.getAuthor().getId(), user.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нельзя откликнуться на это объявление");
            return;
        }

        req.setAttribute("offer", offer);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/jsp/create-request-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User student = (User) req.getSession().getAttribute("user");

        Long offerId;
        Integer subjectId;
        try {
            offerId = Long.parseLong(req.getParameter("offerId"));
            subjectId = Integer.parseInt(req.getParameter("subjectId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные параметры запроса");
            return;
        }

        String message = req.getParameter("message");
        Offer offer = offerService.getOfferById(offerId);

        if (offer == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Объявление не найдено");
            return;
        }

        if (offer.getStatus() != OfferStatus.ACTIVE) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Объявление закрыто");
            return;
        }

        if (Objects.equals(offer.getAuthor().getId(), student.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нельзя откликнуться на собственное объявление");
            return;
        }

        Long attachmentId = null;
        Part filePart = req.getPart("file");
        if (filePart != null && filePart.getSize() > 0 && !filePart.getSubmittedFileName().isEmpty()) {
            CreateFileInfoDto fileInfoDto = CreateFileInfoDto.builder()
                    .originalFileName(filePart.getSubmittedFileName())
                    .size(filePart.getSize())
                    .type(filePart.getContentType())
                    .build();

            try {
                attachmentId = fileInfoService.save(filePart.getInputStream(), fileInfoDto);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        CreateRequestDto createRequestDto = CreateRequestDto.builder()
                .offerId(offerId)
                .subjectId(subjectId)
                .message(message)
                .studentId(student.getId())
                .attachmentId(attachmentId)
                .build();

        try {
            requestService.createRequest(createRequestDto);
            resp.sendRedirect(req.getContextPath() + "/offer?id=" + offerId);
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("offer", offer);
            req.getRequestDispatcher("/jsp/create-request-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

