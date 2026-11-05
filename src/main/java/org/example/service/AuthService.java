package org.example.service;

import org.example.dto.LoginDto;
import org.example.dto.RegistrationDto;
import org.example.models.User;

import java.sql.SQLException;

public interface AuthService {
    void register(RegistrationDto registrationDto) throws SQLException;
    User authenticate(LoginDto loginDto) throws SQLException;

}
