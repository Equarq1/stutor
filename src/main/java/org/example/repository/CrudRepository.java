package org.example.repository;

import java.sql.SQLException;

public interface CrudRepository<T> {
    public void save(T t) throws SQLException;
}
