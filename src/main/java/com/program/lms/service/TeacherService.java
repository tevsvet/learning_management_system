package com.program.lms.service;

import com.program.lms.dao.TeacherRepository;
import com.program.lms.dto.page.PageResponse;
import com.program.lms.dto.teacher.TeacherRequest;
import com.program.lms.dto.teacher.TeacherResponse;
import com.program.lms.dto.teacher.UpdateTeacherRequest;
import com.program.lms.exception.TeacherNotFoundException;
import com.program.lms.mapper.PageMapper;
import com.program.lms.mapper.TeacherMapper;
import com.program.lms.model.TeacherEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final PageMapper pageMapper;

    @Transactional(readOnly = true)
    public PageResponse<TeacherResponse> getAll(Pageable pageable) {

        return pageMapper.toPageResponse(
                teacherRepository.findAll(pageable)
                .map(teacherMapper::toResponse));
    }

    @Transactional
    public TeacherResponse create(TeacherRequest dto) {

        TeacherEntity teacher = TeacherEntity.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .build();

        teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponse update(Long id, UpdateTeacherRequest dto) {

        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        teacherMapper.updateTeacherFromDto(teacher, dto);

        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public void delete(Long id) {

        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

        teacherRepository.delete(teacher);
    }
}