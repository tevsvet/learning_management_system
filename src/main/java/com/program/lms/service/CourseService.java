package com.program.lms.service;

import com.program.lms.dao.CourseRepository;
import com.program.lms.dto.course.CourseRequest;
import com.program.lms.dto.course.CourseResponse;
import com.program.lms.dto.course.CourseShortResponse;
import com.program.lms.dto.course.UpdateCourseRequest;
import com.program.lms.dto.page.PageResponse;
import com.program.lms.exception.CourseNotFoundException;
import com.program.lms.mapper.CourseMapper;
import com.program.lms.mapper.PageMapper;
import com.program.lms.model.CourseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final PageMapper pageMapper;

    @Transactional(readOnly = true)
    public PageResponse<CourseShortResponse> getAll(Pageable pageable) {

        return pageMapper.toPageResponse(
                courseRepository.findAll(pageable)
                .map(courseMapper::toShortResponse));
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