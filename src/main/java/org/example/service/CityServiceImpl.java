package org.example.service;

import org.example.models.City;
import org.example.repository.CityRepository;

import java.sql.SQLException;
import java.util.List;

public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public void addCity(String name) {
        String clean = name == null ? "" : name.trim();
        if (clean.length() < 2 || clean.length() > 100) {
            throw new IllegalArgumentException("Название города должно быть от 2 до 100 символов");
        }
        if (cityRepository.existsByName(clean)) {
            throw new IllegalArgumentException("Такой город уже есть");
        }
        try {
            cityRepository.save(City.builder().name(clean).build());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCity(int id) {
        cityRepository.deleteById(id);
    }
}
