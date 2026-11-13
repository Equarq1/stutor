package org.example.service;

import org.example.models.Subject;

import java.util.List;

public interface SubjectService {
    void save(Subject subject);
    List<Subject> getAllSubjects();
    List<Subject> getAllSubjectsByFacultyIdAndCourse(int facultyId, int course);
}
