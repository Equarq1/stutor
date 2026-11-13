package org.example.repository;

import org.example.models.Faculty;
import org.example.models.Subject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectRepositoryJdbcImpl implements SubjectRepository {

    private final DataSource dataSource;
    private static final String SQL_SELECT_ALL = """
            SELECT s.id, s.name, s.course,
            f.id as faculty_id, f.name as faculty_name
            FROM subjects AS s
            JOIN faculties AS f ON f.id = s.faculty_id
            """;

    private static final String SQL_SELECT_BY_FACULTY_ID_AND_COURSE = SQL_SELECT_ALL + " WHERE f.id = ? AND course = ?";

    public SubjectRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Subject> findAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            List<Subject> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(rowMapperToSubject(resultSet));
            }
            return list;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Subject> findByFacultyAndCourse(Integer facultyId, Integer course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_FACULTY_ID_AND_COURSE)) {
            preparedStatement.setInt(1, facultyId);
            preparedStatement.setInt(2, course);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Subject> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(rowMapperToSubject(resultSet));
            }
            return list;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void save(Subject subject) throws SQLException {

    }

    private Subject rowMapperToSubject(ResultSet resultSet) {
        try {
            Faculty faculty = Faculty.builder()
                    .id(resultSet.getInt("faculty_id"))
                    .name(resultSet.getString("faculty_name"))
                    .build();

            return Subject.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .course(resultSet.getInt("course"))
                    .faculty(faculty)
                    .build();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
