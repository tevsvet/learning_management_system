package com.program.lms.dao;

import com.program.lms.model.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    @EntityGraph(attributePaths = {"group"})
    Page<StudentEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"group"})
    List<StudentEntity> findByGroupId(Long groupId);
}