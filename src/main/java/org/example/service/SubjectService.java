package org.example.service;

import org.example.models.Subject;

import java.util.List;

public interface SubjectService {
    void addSubject(String name, int course, int facultyId);
    void deleteSubject(int id);
    List<Subject> getAllSubjects();
    List<Subject> getAllSubjectsByFacultyIdAndCourse(int facultyId, int course);
}