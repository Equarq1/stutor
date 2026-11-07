package org.example.service;

import org.example.dto.LoginDto;
import org.example.dto.RegistrationDto;
import org.example.models.City;
import org.example.models.StudyGroup;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String KFU_EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@(?:stud\\.)?kpfu\\.ru$";

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void register(RegistrationDto registrationDto) throws SQLException {
        String email = registrationDto.getEmail();

        if (!email.matches(KFU_EMAIL_REGEX)) {
            throw new IllegalArgumentException("Можно зарегистрироваться только с почтой КФУ");
        }

        if ((userRepository.findByEmail(email).isPresent())) {
            throw new IllegalArgumentException("Пользователь с такой почтой уже существует");
        }
        City city = City.builder()
                .id(registrationDto.getCityId())
                .build();

        StudyGroup group = StudyGroup.builder()
                .id(registrationDto.getGroupId())
                .build();

        User user = User.builder()
                .email(email)
                .hashPassword(passwordEncoder.encode(registrationDto.getPassword()))
                .fullName(registrationDto.getFullName())
                .city(city)
                .group(group)
                .build();
        userRepository.save(user);
    }

    @Override
    public User authenticate(LoginDto loginDto) throws SQLException {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("Неверный логин или пароль"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getHashPassword())) {
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
        return user;
    }

}
