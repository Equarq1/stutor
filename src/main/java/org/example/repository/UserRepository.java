package org.example.repository;

import org.example.models.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User> {
    List findAll();
}
