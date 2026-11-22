package org.example.repository;

import org.example.models.Faculty;

import java.util.List;

public interface FacultyRepository extends CrudRepository<Faculty> {
    List<Faculty> findAll();
}
