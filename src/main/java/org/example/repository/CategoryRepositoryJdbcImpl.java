package org.example.repository;

import org.example.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryJdbcImpl implements CategoryRepository {
    private final DataSource dataSource;
    private static final String SQL_SELECT_ALL = "SELECT id, name from offer_categories";

    public CategoryRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Category> findAll() {
        try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
            List<Category> list = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()) {
                list.add(Category.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .build());
            }
            return list;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Category category) throws SQLException {

    }
}
