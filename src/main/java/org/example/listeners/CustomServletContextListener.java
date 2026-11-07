package org.example.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.repository.*;
import org.example.service.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@WebListener
public class CustomServletContextListener implements ServletContextListener {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "pract12345678";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/stutor_db";
    private static final String DB_DRIVER = "org.postgresql.Driver";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setUrl(DB_URL);

        // Репозитории
        UserRepositoryJdbcImpl userRepository = new UserRepositoryJdbcImpl(dataSource);
        FacultyRepository facultyRepository = new FacultyRepositoryJdbcImpl(dataSource);
        CityRepository cityRepository = new CityRepositoryJdbcImpl(dataSource);
        StudyGroupRepository studyGroupRepository = new StudyGroupRepositoryJdbcImpl(dataSource);
        OfferRepository offerRepository = new OfferRepositoryJdbcImpl(dataSource);

        // Сервисы
        AuthService authService = new AuthServiceImpl(userRepository);
        FacultyService facultyService = new FacultyServiceImpl(facultyRepository);
        CityService cityService = new CityServiceImpl(cityRepository);
        StudyGroupService studyGroupService = new StudyGroupServiceImpl(studyGroupRepository);
        OfferService offerService = new OfferServiceImpl(offerRepository);

        // Добавляем репозитории в контекст

        servletContext.setAttribute("userRepository", userRepository);
        servletContext.setAttribute("facultyRepository", facultyRepository);
        servletContext.setAttribute("cityRepository", cityRepository);
        servletContext.setAttribute("studyGroupRepository", studyGroupRepository);
        servletContext.setAttribute("offerRepository", offerRepository);

        // Добавляем сервисы в контекст
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("facultyService", facultyService);
        servletContext.setAttribute("cityService", cityService);
        servletContext.setAttribute("studyGroupService", studyGroupService);
        servletContext.setAttribute("offerService", offerService);


    }
}
