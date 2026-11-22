package org.example.service;

import org.example.models.User;
import org.example.models.enums.UserStatus;

import java.util.List;

public interface UserService {
    void updateProfile(User user);
    User getUserByEmail(String email);
    User getUserById(Long id);
    List<User> getAllUsers();
    void changeStatus(Long userId, UserStatus status);
}
