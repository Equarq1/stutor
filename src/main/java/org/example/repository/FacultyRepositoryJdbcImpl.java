package org.example.repository;

import org.example.models.Faculty;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyRepositoryJdbcImpl implements FacultyRepository{
    private final DataSource dataSource;
    private static final String SQL_SELECT_ALL = "SELECT * FROM faculties";

    public FacultyRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public List<Faculty> findAll() {
        try (Connection connection = dataSource.getConnection()
        ) {
            List<Faculty> list = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            while (resultSet.next()) {
                Faculty faculty = Faculty.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .build();
                list.add(faculty);
            }
            return list;


        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
