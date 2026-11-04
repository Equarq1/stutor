package org.example.service;

import org.example.models.Faculty;
import org.example.repository.FacultyRepository;

import java.util.List;

public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
}
