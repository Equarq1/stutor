package org.example.repository;

import org.example.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    List<User> findAll();
    Optional<User> findByEmail(String email);
}
