-- Stutor: стартовые справочные данные
-- Запускать после schema.sql на пустой базе.
-- Id выдаются последовательностями по порядку вставки (1, 2, 3...).

INSERT INTO cities (name) VALUES
    ('Казань'),
    ('Челны'),
    ('Елабуга'),
    ('Нижнекамск');

INSERT INTO faculties (name) VALUES
    ('ИТИС'),
    ('ИВМИТ'),
    ('ИМИМ');

INSERT INTO study_groups (name, faculty_id, admission_year) VALUES
    ('11-504', 1, 2025),
    ('11-402', 1, 2024),
    ('11-603', 1, 2026),
    ('09-552', 2, 2025),
    ('09-553', 2, 2025);

INSERT INTO offer_categories (name) VALUES
    ('Помощь с ДЗ'),
    ('Помощь с семестровой'),
    ('Подготовка к сессии'),
    ('Объяснение лекций'),
    ('Помощь с курсовой'),
    ('Другое');

INSERT INTO subjects (name, course, faculty_id) VALUES
    ('ОРИС', 2, 1),
    ('АиСД', 1, 1),
    ('Матан', 1, 1),
    ('Дискретная математика', 1, 1),
    ('БД', 2, 1),
    ('Архитектура ЭВМ', 2, 2),
    ('Высшая математика', 1, 2),
    ('Линейная алгебра', 1, 3),
    ('Теория вероятностей', 2, 3),
    ('Сопромат', 2, 3);

-- Администратор. Без него после чистой установки в админку не зайти.
-- Вместо PLACEHOLDER_BCRYPT_HASH вставь BCrypt-хеш своего пароля
-- (его можно сгенерировать через BCryptPasswordEncoder.encode("пароль")).
-- Раскомментируй после подстановки хеша:
--
-- INSERT INTO users (email, password_hash, full_name, created_at, role, status, city_id, group_id)
-- VALUES ('admin@kpfu.ru', 'PLACEHOLDER_BCRYPT_HASH', 'Админ', NOW(), 'ADMIN', 'ACTIVE', 1, 1);
