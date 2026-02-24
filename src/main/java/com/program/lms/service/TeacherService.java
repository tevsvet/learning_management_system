package com.program.lms.service;

import com.program.lms.dao.TeacherRepository;
import com.program.lms.dto.teacher.TeacherRequest;
import com.program.lms.dto.teacher.TeacherResponse;
import com.program.lms.dto.teacher.UpdateTeacherRequest;
import com.program.lms.exception.TeacherNotFoundException;
import com.program.lms.mapper.TeacherMapper;
import com.program.lms.model.TeacherEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {

        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toResponse)
                .toList();
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