package com.program.lms.dao;

import com.program.lms.model.TeacherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {

    Page<TeacherEntity> findAll(Pageable pageable);
}