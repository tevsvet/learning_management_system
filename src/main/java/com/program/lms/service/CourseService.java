package com.program.lms.service;

import com.program.lms.dao.CourseRepository;
import com.program.lms.dto.course.CourseRequest;
import com.program.lms.dto.course.CourseResponse;
import com.program.lms.dto.course.UpdateCourseRequest;
import com.program.lms.exception.CourseNotFoundException;
import com.program.lms.mapper.CourseMapper;
import com.program.lms.model.CourseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {

        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse create(CourseRequest dto) {

        CourseEntity course = CourseEntity.builder()
                .name(dto.name())
                .build();

        String description = dto.description();
        course.setDescription(
                description == null || description.isBlank() ? null : description);

        courseRepository.save(course);

        return courseMapper.toResponse(course);
    }

    @Transactional
    public CourseResponse update(Long id, UpdateCourseRequest dto) {

        CourseEntity course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        courseMapper.updateCourseFromDto(course, dto);

        return courseMapper.toResponse(course);
    }

    @Transactional
    public void delete(Long id) {

        CourseEntity course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        courseRepository.delete(course);
    }
}