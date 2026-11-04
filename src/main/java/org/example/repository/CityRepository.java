package org.example.repository;

import org.example.models.City;

import java.util.List;

public interface CityRepository {
    List<City> findAll();
}
