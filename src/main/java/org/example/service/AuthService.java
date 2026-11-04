package org.example.service;

import org.example.dto.RegistrationDto;

import java.sql.SQLException;

public interface AuthService {
    void register(RegistrationDto registrationDto) throws SQLException;

}
