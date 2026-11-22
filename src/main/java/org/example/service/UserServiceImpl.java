package org.example.service;

import org.example.models.User;
import org.example.models.enums.UserStatus;
import org.example.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void updateProfile(User user) {
        userRepository.updateProfile(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void changeStatus(Long userId, UserStatus status) {
        userRepository.updateStatus(userId, status);
    }
}