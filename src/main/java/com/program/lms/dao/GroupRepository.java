package com.program.lms.dao;

import com.program.lms.model.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    Page<GroupEntity> findAll(Pageable pageable);
}