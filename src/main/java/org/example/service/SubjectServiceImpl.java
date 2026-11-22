package org.example.service;

import org.example.models.Faculty;
import org.example.models.Subject;
import org.example.repository.SubjectRepository;

import java.sql.SQLException;
import java.util.List;

public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void addSubject(String name, int course, int facultyId) {
        String clean = name == null ? "" : name.trim();
        if (clean.length() < 2 || clean.length() > 200) {
            throw new IllegalArgumentException("Название предмета должно быть от 2 до 200 символов");
        }
        if (course < 1 || course > 4) {
            throw new IllegalArgumentException("Курс должен быть от 1 до 4");
        }
        if (subjectRepository.exists(facultyId, course, clean)) {
            throw new IllegalArgumentException("Такой предмет уже добавлен для этого института и курса");
        }
        Subject subject = Subject.builder()
                .name(clean)
                .course(course)
                .faculty(Faculty.builder().id(facultyId).build())
                .build();
        try {
            subjectRepository.save(subject);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public List<Subject> getAllSubjectsByFacultyIdAndCourse(int facultyId, int course) {
        return subjectRepository.findByFacultyAndCourse(facultyId, course);
    }

    @Override
    public void deleteSubject(int id) {
        if (subjectRepository.isUsedInOffers(id)) {
            throw new IllegalArgumentException("Нельзя удалить предмет, пока он указан в объявлениях");
        }
        subjectRepository.deleteById(id);
    }
}