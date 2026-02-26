--liquibase formatted sql

--changeset tevs:004
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,

    group_id BIGINT NOT NULL

    CONSTRAINT fk_student_group FOREIGN KEY (group_id) REFERENCES groups(id)
);