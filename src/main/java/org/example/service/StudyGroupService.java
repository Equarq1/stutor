package org.example.service;

import org.example.models.StudyGroup;
import org.example.repository.StudyGroupRepository;

import java.util.List;

public interface StudyGroupService {
    List<StudyGroup> getStudyGroupsByFacultyAndYear(Integer facultyId, Integer year);
}
