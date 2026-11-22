package org.example.service;

import org.example.models.City;

import java.util.List;

public interface CityService {
    List<City> getAllCities();
    void addCity(String name);
    void deleteCity(int id);
}