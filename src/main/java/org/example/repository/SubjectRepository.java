package org.example.repository;

import org.example.models.Subject;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subject> {
    List<Subject> findAll();
    List<Subject> findByFacultyAndCourse(Integer facultyId, Integer course);
    void deleteById(int id);
    boolean exists(Integer facultyId, Integer course, String name);
    boolean isUsedInOffers(int id);
}