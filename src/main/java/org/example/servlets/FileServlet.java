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
        Long id = Long.parseLong(req.getParameter("id"));
        FileInfo fileInfo = fileInfoService.getFileInfoById(id);
        User currentUser = (User) req.getSession().getAttribute("user");

        if (!fileInfoService.canUserAccess(id, currentUser)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (fileInfo != null) {
            resp.setContentType(fileInfo.getType());
            String encodedFilename = URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            String disposition = fileInfo.getType().startsWith("image/") ? "inline" : "attachment";

            resp.setHeader("Content-Disposition", disposition + "; filename*=UTF-8''" + encodedFilename);
            fileInfoService.writeFileFromStorage(id, resp.getOutputStream());
        }
    }

}
