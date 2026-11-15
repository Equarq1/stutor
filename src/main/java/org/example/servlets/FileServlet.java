package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.FileInfo;
import org.example.service.FileInfoService;

import java.io.IOException;

@WebServlet("/file")
public class FileServlet extends HttpServlet {

    private FileInfoService fileInfoService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        fileInfoService = (FileInfoService) config.getServletContext().getAttribute("fileInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        FileInfo fileInfo = fileInfoService.getFileInfoById(id);

        if (fileInfo != null) {
            resp.setContentType(fileInfo.getType());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileInfo.getOriginalFileName() + "\"");
            fileInfoService.writeFileFromStorage(id, resp.getOutputStream());
        }
    }

}
