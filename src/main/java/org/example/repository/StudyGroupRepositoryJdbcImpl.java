package org.example.repository;

import org.example.models.Faculty;
import org.example.models.StudyGroup;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyGroupRepositoryJdbcImpl implements  StudyGroupRepository {
    private final DataSource dataSource;
    private static final String SQL_SELECT_GROUPS_BY_FACULTY_AND_YEAR = """
            SELECT g.id, g.name, g.admission_year,
                   f.id as faculty_id, f.name as faculty_name
                   FROM study_groups as g
            LEFT JOIN faculties AS f ON g.faculty_id = f.id
            WHERE g.admission_year = ? AND g.faculty_id = ?""";

    public StudyGroupRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<StudyGroup> findGroupsByFacultyAndYear(Integer facultyId, Integer year) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GROUPS_BY_FACULTY_AND_YEAR);) {
            List<StudyGroup> list = new ArrayList<>();
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, facultyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudyGroup studyGroup = rowMapperToStudyGroup(resultSet);
                list.add(studyGroup);
            }
            return list;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private StudyGroup rowMapperToStudyGroup(ResultSet resultSet) throws SQLException {
        Faculty faculty = Faculty.builder()
                .id(resultSet.getInt("faculty_id"))
                .name(resultSet.getString("faculty_name"))
                .build();

        return StudyGroup.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .faculty(faculty)
                .admissionYear(resultSet.getInt("admission_year"))
                .build();


    }
}
