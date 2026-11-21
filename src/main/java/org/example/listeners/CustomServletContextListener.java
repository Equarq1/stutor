package org.example.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.repository.*;
import org.example.service.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebListener
public class CustomServletContextListener implements ServletContextListener {
    private static final String DB_PROPERTIES_FILE = "db.properties";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Properties dbProperties = loadDbProperties();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbProperties.getProperty("db.driver"));
        dataSource.setUrl(dbProperties.getProperty("db.url"));
        dataSource.setUsername(dbProperties.getProperty("db.username"));
        dataSource.setPassword(dbProperties.getProperty("db.password"));

        // Репозитории
        UserRepositoryJdbcImpl userRepository = new UserRepositoryJdbcImpl(dataSource);
        FacultyRepository facultyRepository = new FacultyRepositoryJdbcImpl(dataSource);
        CityRepository cityRepository = new CityRepositoryJdbcImpl(dataSource);
        StudyGroupRepository studyGroupRepository = new StudyGroupRepositoryJdbcImpl(dataSource);
        OfferRepository offerRepository = new OfferRepositoryJdbcImpl(dataSource);
        CategoryRepository categoryRepository = new CategoryRepositoryJdbcImpl(dataSource);
        SubjectRepository subjectRepository = new SubjectRepositoryJdbcImpl(dataSource);
        FileInfoRepository fileInfoRepository = new FileInfoRepositoryJdbcImpl(dataSource);
        RequestRepository requestRepository = new RequestRepositoryJdbcImpl(dataSource);
        ReviewRepository reviewRepository = new ReviewRepositoryJdbcImpl(dataSource);

        // Сервисы
        AuthService authService = new AuthServiceImpl(userRepository);
        FacultyService facultyService = new FacultyServiceImpl(facultyRepository);
        CityService cityService = new CityServiceImpl(cityRepository);
        StudyGroupService studyGroupService = new StudyGroupServiceImpl(studyGroupRepository);
        OfferService offerService = new OfferServiceImpl(offerRepository);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository);
        SubjectService subjectService = new SubjectServiceImpl(subjectRepository);
        FileInfoService fileInfoService = new FileInfoServiceImpl(fileInfoRepository);
        UserService userService = new UserServiceImpl(userRepository);
        RequestService requestService = new RequestServiceImpl(requestRepository);
        ReviewService reviewService = new ReviewServiceImpl(reviewRepository, requestRepository);

        // Добавляем репозитории в контекст
        servletContext.setAttribute("userRepository", userRepository);
        servletContext.setAttribute("facultyRepository", facultyRepository);
        servletContext.setAttribute("cityRepository", cityRepository);
        servletContext.setAttribute("studyGroupRepository", studyGroupRepository);
        servletContext.setAttribute("offerRepository", offerRepository);
        servletContext.setAttribute("categoryRepository", categoryRepository);
        servletContext.setAttribute("subjectRepository", subjectRepository);
        servletContext.setAttribute("fileInfoRepository", fileInfoRepository);
        servletContext.setAttribute("requestRepository", requestRepository);
        servletContext.setAttribute("reviewRepository", reviewRepository);

        // Добавляем сервисы в контекст
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("facultyService", facultyService);
        servletContext.setAttribute("cityService", cityService);
        servletContext.setAttribute("studyGroupService", studyGroupService);
        servletContext.setAttribute("offerService", offerService);
        servletContext.setAttribute("categoryService", categoryService);
        servletContext.setAttribute("subjectService", subjectService);
        servletContext.setAttribute("fileInfoService", fileInfoService);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("requestService", requestService);
        servletContext.setAttribute("reviewService", reviewService);

    }

    private Properties loadDbProperties() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        DB_PROPERTIES_FILE + " not found. Copy db.properties.example to db.properties and fill it in.");
            }
            Properties properties = new Properties();
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + DB_PROPERTIES_FILE, e);
        }
    }
}
