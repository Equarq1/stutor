package org.example.repository;

import org.example.models.Faculty;

import java.util.List;

public interface FacultyRepository {
    List<Faculty> findAll();
}
