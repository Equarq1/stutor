package org.example.service;

import org.example.models.Subject;
import org.example.repository.SubjectRepository;

import java.util.List;

public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void save(Subject subject) {

    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public List<Subject> getAllSubjectsByFacultyIdAndCourse(int facultyId, int course) {
        return subjectRepository.findByFacultyAndCourse(facultyId, course);
    }
}
