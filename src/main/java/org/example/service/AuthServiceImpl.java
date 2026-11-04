package org.example.service;

import org.example.dto.RegistrationDto;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void register(RegistrationDto registrationDto) throws SQLException {
        User user = User.builder()
                .email(registrationDto.getEmail())
                .hashPassword(passwordEncoder.encode(registrationDto.getPassword()))
                .fullName(registrationDto.getFullName())
                .cityId(registrationDto.getCityId())
                .groupId(registrationDto.getGroupId())
                .build();
        userRepository.save(user);
    }
}
