package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.models.User;
import org.example.service.FileInfoService;
import org.example.service.UserService;

import java.io.IOException;

@WebServlet("/profile/update")
@MultipartConfig(maxFileSize = 10485760)
public class UpdateProfileServlet extends HttpServlet {
    private UserService userService;
    private FileInfoService fileInfoService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute("userService");
        fileInfoService = (FileInfoService) config.getServletContext().getAttribute("fileInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/update-my-profile-page.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        User currentUser = (User) req.getSession().getAttribute("user");

        String about = req.getParameter("about");
        String contact = req.getParameter("contact");
        Long avatarId = currentUser.getAvatar() != null ? currentUser.getAvatar().getId() : null;

        Part filePart = req.getPart("avatar");
        if (filePart != null && filePart.getSize() > 0 && !filePart.getSubmittedFileName().isEmpty()) {
            try {
                org.example.dto.CreateFileInfoDto fileDto = org.example.dto.CreateFileInfoDto.builder()
                        .originalFileName(filePart.getSubmittedFileName())
                        .size(filePart.getSize())
                        .type(filePart.getContentType())
                        .build();
                avatarId = fileInfoService.save(filePart.getInputStream(), fileDto);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка сохранения аватарки", e);
            }
        }

        currentUser.setAbout(about);
        currentUser.setContact(contact);
        if (avatarId != null) {
            currentUser.setAvatar(org.example.models.FileInfo.builder().id(avatarId).build());
        }

        userService.updateProfile(currentUser);
        User updatedUser = userService.getUserByEmail(currentUser.getEmail());
        req.getSession().setAttribute("user", updatedUser);

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}