package org.example.repository;

import org.example.models.Faculty;
import org.example.models.StudyGroup;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyGroupRepositoryJdbcImpl implements  StudyGroupRepository {
    private final DataSource dataSource;
    private static final String SQL_SELECT_GROUPS_BY_FACULTY_AND_YEAR = "SELECT * FROM study_groups" +
            " WHERE admission_year = ? AND faculty_id = ?";

    public StudyGroupRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<StudyGroup> findGroupsByFacultyAndYear(Integer facultyId, Integer year) {
        try (Connection connection = dataSource.getConnection()) {
            List<StudyGroup> list = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GROUPS_BY_FACULTY_AND_YEAR);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, facultyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudyGroup studyGroup = StudyGroup.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .facultyId(resultSet.getInt("faculty_id"))
                        .admissionYear(resultSet.getInt("admission_year"))
                        .build();
                list.add(studyGroup);
            }
            return list;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
