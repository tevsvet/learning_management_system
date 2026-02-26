--liquibase formatted sql

--changeset tevs:003
CREATE TABLE teachers (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);
