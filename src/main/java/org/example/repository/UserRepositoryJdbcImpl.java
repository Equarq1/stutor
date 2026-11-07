package org.example.repository;

import org.example.models.*;
import org.example.models.enums.Role;
import org.example.models.enums.UserStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository{
    private DataSource dataSource;
    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String SQL_SELECT_BY_EMAIL = """
        SELECT u.id, u.email, u.password_hash, u.full_name, u.about, u.contact, u.created_at, u.role, u.status,
               c.name as city_name, c.id as city_id,
               g.name as group_name, g.id as group_id, g.admission_year,
               f.storage_file_name as file_name, f.id as file_id,
               fac.name as faculty_name, fac.id as faculty_id
        FROM users AS u
        JOIN cities AS c ON c.id = u.city_id
        JOIN study_groups AS g ON g.id = u.group_id
        LEFT JOIN file_info AS f ON f.id = u.avatar_id
        JOIN faculties AS fac ON fac.id = g.faculty_id
        WHERE u.email = ?    
        """;

    private static final String SQL_INSERT_INTO_USERS = "INSERT into users" +
            " (email, password_hash, full_name, about," +
            "contact, created_at, role, status, city_id, group_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";



    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(rowMapperToUser(resultSet));
            }
            return Optional.empty();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_USERS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getHashPassword());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getAbout());
            preparedStatement.setString(5, user.getContact());
            preparedStatement.setObject(6, LocalDateTime.now());
            preparedStatement.setString(7, Role.USER.name());
            preparedStatement.setString(8, UserStatus.ACTIVE.name());
            preparedStatement.setInt(9, user.getCity().getId());
            preparedStatement.setInt(10, user.getGroup().getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public User rowMapperToUser(ResultSet resultSet) throws SQLException {
        FileInfo fileInfo = null;

        if (resultSet.getObject("file_id") != null) {
            fileInfo = FileInfo.builder()
                    .id(resultSet.getLong("file_id"))
                    .storageFileName(resultSet.getString("file_name"))
                    .build();
        }

        City city = City.builder()
                .id(resultSet.getInt("city_id"))
                .name(resultSet.getString("city_name"))
                .build();

        Faculty faculty = Faculty.builder()
                .id(resultSet.getInt("faculty_id"))
                .name(resultSet.getString("faculty_name"))
                .build();

        StudyGroup studyGroup = StudyGroup.builder()
                .id(resultSet.getInt("group_id"))
                .name(resultSet.getString("group_name"))
                .faculty(faculty)
                .admissionYear(resultSet.getInt("admission_year"))
                .build();

        return User.builder()
                        .id(resultSet.getLong("id"))
                        .email(resultSet.getString("email"))
                        .hashPassword(resultSet.getString("password_hash"))
                        .fullName(resultSet.getString("full_name"))
                        .about(resultSet.getString("about"))
                        .contact(resultSet.getString("contact"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .role(Role.valueOf(resultSet.getString("role")))
                        .userStatus(UserStatus.valueOf(resultSet.getString("status")))
                        .city(city)
                        .group(studyGroup)
                        .avatar(fileInfo)
                        .build();

    }

}
