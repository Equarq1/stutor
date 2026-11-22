-- Stutor: схема базы данных (PostgreSQL)
-- Порядок запуска: сначала schema.sql, затем data.sql

CREATE TABLE file_info (
    id                 BIGSERIAL PRIMARY KEY,
    original_file_name VARCHAR(255) NOT NULL,
    storage_file_name  VARCHAR(255) NOT NULL UNIQUE,
    size               BIGINT       NOT NULL,
    type               VARCHAR(100) NOT NULL,
    created_at         TIMESTAMP    NOT NULL
);

CREATE TABLE cities (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE faculties (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE study_groups (
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(50) NOT NULL,
    faculty_id     INT         NOT NULL REFERENCES faculties (id) ON DELETE CASCADE,
    admission_year SMALLINT    NOT NULL,
    UNIQUE (name, faculty_id, admission_year)
);

CREATE TABLE offer_categories (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE subjects (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(200) NOT NULL,
    course     SMALLINT     NOT NULL,
    faculty_id INT          NOT NULL REFERENCES faculties (id) ON DELETE CASCADE,
    UNIQUE (faculty_id, course, name)
);

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(150) NOT NULL,
    about         TEXT,
    contact       VARCHAR(200),
    created_at    TIMESTAMP    NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    city_id       INT    REFERENCES cities (id) ON DELETE SET NULL,
    group_id      INT    REFERENCES study_groups (id) ON DELETE SET NULL,
    avatar_id     BIGINT REFERENCES file_info (id) ON DELETE SET NULL
);

CREATE TABLE offers (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(150)  NOT NULL,
    description   TEXT          NOT NULL,
    price         NUMERIC(8, 2) NOT NULL,
    created_at    TIMESTAMP     NOT NULL,
    format        VARCHAR(20)   NOT NULL DEFAULT 'BOTH',
    status        VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    author_id     BIGINT        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    category_id   INT           NOT NULL REFERENCES offer_categories (id),
    attachment_id BIGINT        REFERENCES file_info (id) ON DELETE SET NULL
);

CREATE TABLE offer_subjects (
    offer_id   BIGINT NOT NULL REFERENCES offers (id) ON DELETE CASCADE,
    subject_id INT    NOT NULL REFERENCES subjects (id) ON DELETE CASCADE,
    PRIMARY KEY (offer_id, subject_id)
);

CREATE TABLE requests (
    id            BIGSERIAL PRIMARY KEY,
    offer_id      BIGINT      NOT NULL,
    subject_id    INT         NOT NULL,
    message       TEXT,
    created_at    TIMESTAMP   NOT NULL,
    status        VARCHAR(20) NOT NULL,
    student_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    attachment_id BIGINT      REFERENCES file_info (id) ON DELETE SET NULL,
    FOREIGN KEY (offer_id, subject_id) REFERENCES offer_subjects (offer_id, subject_id) ON DELETE CASCADE,
    UNIQUE (offer_id, subject_id, student_id)
);

CREATE TABLE reviews (
    id         BIGSERIAL PRIMARY KEY,
    request_id BIGINT UNIQUE REFERENCES requests (id) ON DELETE SET NULL,
    tutor_id   BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    student_id BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    rating     SMALLINT  NOT NULL,
    comment    TEXT,
    created_at TIMESTAMP NOT NULL
);
