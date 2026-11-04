package org.example.service;

import org.example.models.StudyGroup;
import org.example.repository.StudyGroupRepository;

import java.util.List;

public class StudyGroupServiceImpl implements StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    public StudyGroupServiceImpl(StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }

    @Override
    public List<StudyGroup> getStudyGroupsByFacultyAndYear(Integer facultyId, Integer year) {
        return studyGroupRepository.findGroupsByFacultyAndYear(facultyId, year);
    }
}
