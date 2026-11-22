package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.CategoryService;

import java.io.IOException;

@WebServlet("/admin/categories")
public class AdminCategoriesServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        categoryService = (CategoryService) config.getServletContext().getAttribute("categoryService");
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
                categoryService.addCategory(req.getParameter("name"));
            } else if ("delete".equals(action)) {
                categoryService.deleteCategory(Integer.parseInt(req.getParameter("categoryId")));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неизвестное действие");
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID категории");
            return;
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showPage(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", categoryService.getAllCategories());
        req.getRequestDispatcher("/jsp/admin-categories.jsp").forward(req, resp);
    }
}