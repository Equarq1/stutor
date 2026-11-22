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

    private static final String SQL_SELECT_ALL_ORDERED = SQL_SELECT_ALL + " ORDER BY f.name, s.course, s.name";
    private static final String SQL_SELECT_BY_FACULTY_ID_AND_COURSE = SQL_SELECT_ALL + " WHERE f.id = ? AND course = ?";
    private static final String SQL_INSERT = "INSERT INTO subjects (name, course, faculty_id) VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM subjects WHERE id = ?";
    private static final String SQL_EXISTS = "SELECT 1 FROM subjects WHERE faculty_id = ? AND course = ? AND LOWER(name) = LOWER(?)";
    private static final String SQL_IS_USED = "SELECT 1 FROM offer_subjects WHERE subject_id = ? LIMIT 1";



    public SubjectRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Subject> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ORDERED);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Subject> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(rowMapperToSubject(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Subject> findByFacultyAndCourse(Integer facultyId, Integer course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_FACULTY_ID_AND_COURSE)) {
            preparedStatement.setInt(1, facultyId);
            preparedStatement.setInt(2, course);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Subject> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(rowMapperToSubject(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Subject subject) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {
            ps.setString(1, subject.getName());
            ps.setInt(2, subject.getCourse());
            ps.setInt(3, subject.getFaculty().getId());
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
    public boolean exists(Integer facultyId, Integer course, String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_EXISTS)) {
            ps.setInt(1, facultyId);
            ps.setInt(2, course);
            ps.setString(3, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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