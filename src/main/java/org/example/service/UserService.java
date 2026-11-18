package org.example.service;

import org.example.models.User;

public interface UserService {
    void updateProfile(User user);
    User getUserByEmail(String email);
    User getUserById(Long id);
}
