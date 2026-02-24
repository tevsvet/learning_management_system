package com.program.lms.service;

import com.program.lms.dao.GroupRepository;
import com.program.lms.dao.StudentRepository;
import com.program.lms.dto.student.StudentRequest;
import com.program.lms.dto.student.StudentResponse;
import com.program.lms.dto.student.UpdateStudentRequest;
import com.program.lms.exception.GroupNotFoundException;
import com.program.lms.exception.StudentNotFoundException;
import com.program.lms.mapper.StudentMapper;
import com.program.lms.model.GroupEntity;
import com.program.lms.model.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {

        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Transactional
    public StudentResponse create(StudentRequest dto) {

        GroupEntity group = groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not Found"));

        StudentEntity student = StudentEntity.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .group(group)
                .build();

        studentRepository.save(student);

        return studentMapper.toResponse(student);
    }

    @Transactional
    public StudentResponse update(Long id, UpdateStudentRequest dto) {

        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        studentMapper.updateStudentFromDto(student, dto);

        if (dto.groupId() != null) {
            GroupEntity group = groupRepository.findById(dto.groupId())
                    .orElseThrow(() -> new GroupNotFoundException("Group not found"));
            student.setGroup(group);
        }

        return studentMapper.toResponse(student);
    }

    @Transactional
    public void delete(Long id) {

        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        studentRepository.delete(student);
    }
}