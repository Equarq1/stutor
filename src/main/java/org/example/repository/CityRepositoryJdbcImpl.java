package org.example.repository;

import org.example.models.City;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityRepositoryJdbcImpl implements CityRepository {
    private final DataSource dataSource;
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM cities";

    public CityRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    private static final String SQL_INSERT = "INSERT INTO cities (name) VALUES (?)";
    private static final String SQL_DELETE = "DELETE FROM cities WHERE id = ?";
    private static final String SQL_EXISTS_BY_NAME = "SELECT 1 FROM cities WHERE LOWER(name) = LOWER(?)";

    @Override
    public void save(City city) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, city.getName());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<City> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<City> list = new ArrayList<>();
            while (resultSet.next()) {
                City city = City.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .build();
                list.add(city);
            }
            return list;
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
}
