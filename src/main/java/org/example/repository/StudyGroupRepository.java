package org.example.repository;

import org.example.models.Faculty;
import org.example.models.StudyGroup;

import java.util.List;

public interface StudyGroupRepository {
    List<StudyGroup> findGroupsByFacultyAndYear(Integer facultyId, Integer year);
}
