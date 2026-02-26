--liquibase formatted sql

--changeset tevs:005
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,

    group_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,

    CONSTRAINT fk_lesson_group FOREIGN KEY (group_id) REFERENCES groups(id),
    CONSTRAINT fk_lesson_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_lesson_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);
