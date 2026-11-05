package org.example.repository;

import org.example.models.User;
import org.example.models.enums.Role;
import org.example.models.enums.UserStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository{
    private DataSource dataSource;
    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    private static final String SQL_INSERT_INTO_USERS = "INSERT into users" +
            " (email, password_hash, full_name, about," +
            "contact, created_at, role, status, city_id, group_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of((User.builder()
                        .id(resultSet.getLong("id"))
                        .email(resultSet.getString("email"))
                        .hashPassword(resultSet.getString("password_hash"))
                        .fullName(resultSet.getString("full_name"))
                        .about(resultSet.getString("about"))
                        .contact(resultSet.getString("contact"))
                        .createdAt((resultSet.getTimestamp("created_at")).toLocalDateTime())
                        .role(Role.valueOf(resultSet.getString("role")))
                        .userStatus(UserStatus.valueOf(resultSet.getString("status")))
                        .cityId(resultSet.getInt("city_id"))
                        .groupId(resultSet.getInt("group_id"))
                        .avatarId(resultSet.getInt("avatar_id"))
                        .build()));
            }
            return Optional.empty();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_USERS);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getHashPassword());
        preparedStatement.setString(3, user.getFullName());
        preparedStatement.setString(4, user.getAbout());
        preparedStatement.setString(5, user.getContact());
        preparedStatement.setObject(6, LocalDateTime.now());
        preparedStatement.setString(7, Role.USER.name());
        preparedStatement.setString(8, UserStatus.ACTIVE.name());
        preparedStatement.setInt(9, user.getCityId());
        preparedStatement.setInt(10, user.getGroupId());
        preparedStatement.executeUpdate();

    }
}
