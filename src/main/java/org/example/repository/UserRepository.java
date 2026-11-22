package org.example.repository;

import org.example.models.User;
import org.example.models.enums.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    List<User> findAll();
    Optional<User> findByEmail(String email);
    void updateProfile(User user);
    Optional<User> findById(Long id);
    void updateStatus(Long userId, UserStatus status);
}
