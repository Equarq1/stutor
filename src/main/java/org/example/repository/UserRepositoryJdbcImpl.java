package org.example.repository;

import org.example.models.User;
import org.example.models.enums.Role;
import org.example.models.enums.UserStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UserRepositoryJdbcImpl implements UserRepository{
    private DataSource dataSource;
    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String SQL_INSERT_INTO_USERS = "INSERT into users" +
            " (email, password_hash, full_name, about," +
            "contact, created_at, role, status, city_id, group_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List findAll() {
        return List.of();
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
