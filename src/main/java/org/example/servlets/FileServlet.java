package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.FileInfo;
import org.example.models.User;
import org.example.service.FileInfoService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/file")
public class    FileServlet extends HttpServlet {

    private FileInfoService fileInfoService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        fileInfoService = (FileInfoService) config.getServletContext().getAttribute("fileInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID файла");
            return;
        }

        FileInfo fileInfo = fileInfoService.getFileInfoById(id);
        if (fileInfo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("user");
        if (!fileInfoService.canUserAccess(id, currentUser)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String contentType = fileInfo.getType() != null ? fileInfo.getType() : "application/octet-stream";
        resp.setContentType(contentType);
        String encodedFilename = URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        String disposition = contentType.startsWith("image/") ? "inline" : "attachment";

        resp.setHeader("Content-Disposition", disposition + "; filename*=UTF-8''" + encodedFilename);
        fileInfoService.writeFileFromStorage(id, resp.getOutputStream());
    }

}
