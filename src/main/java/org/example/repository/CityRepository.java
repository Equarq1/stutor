package org.example.repository;

import org.example.models.City;

import java.util.List;

public interface CityRepository extends CrudRepository<City> {
    List<City> findAll();
    void deleteById(int id);
    boolean existsByName(String name);
}
