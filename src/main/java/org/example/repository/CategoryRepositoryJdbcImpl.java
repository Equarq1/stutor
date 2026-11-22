package org.example.repository;

import org.example.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryJdbcImpl implements CategoryRepository {
    private final DataSource dataSource;
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM offer_categories ORDER BY id";
    private static final String SQL_INSERT = "INSERT INTO offer_categories (name) VALUES (?)";
    private static final String SQL_DELETE = "DELETE FROM offer_categories WHERE id = ?";
    private static final String SQL_EXISTS_BY_NAME = "SELECT 1 FROM offer_categories WHERE LOWER(name) = LOWER(?)";
    private static final String SQL_IS_USED = "SELECT 1 FROM offers WHERE category_id = ? LIMIT 1";

    public CategoryRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Category> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(Category.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .build());
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Category category) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_EXISTS_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isUsedInOffers(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_IS_USED)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}