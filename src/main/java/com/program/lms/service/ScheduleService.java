package com.program.lms.service;

import com.program.lms.dao.*;
import com.program.lms.domain.schedule.ScheduleCalculator;
import com.program.lms.domain.schedule.ScheduleValidator;
import com.program.lms.dto.page.PageResponse;
import com.program.lms.dto.schedule.ScheduleRequest;
import com.program.lms.dto.schedule.ScheduleResponse;
import com.program.lms.dto.schedule.UpdateScheduleRequest;
import com.program.lms.exception.*;
import com.program.lms.mapper.PageMapper;
import com.program.lms.mapper.ScheduleMapper;
import com.program.lms.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleMapper scheduleMapper;
    private final PageMapper pageMapper;
    private final ScheduleCalculator scheduleCalculator;
    private final ScheduleValidator scheduleValidator;

    @Transactional(readOnly = true)
    public PageResponse<ScheduleResponse> getAll(Pageable pageable) {

        return pageMapper.toPageResponse(
                scheduleRepository.findAll(pageable)
                .map(scheduleMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ScheduleResponse> getAllByGroup(Long groupId, Pageable pageable) {

        groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not Found"));

        return pageMapper.toPageResponse(
                scheduleRepository.findByGroupId(groupId, pageable)
                .map(scheduleMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ScheduleResponse> getAllByTeacher(Long teacherId, Pageable pageable) {

        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not Found"));

        return pageMapper.toPageResponse(
                scheduleRepository.findByTeacherId(teacherId, pageable)
                .map(scheduleMapper::toResponse));
    }

    @Transactional
    public ScheduleResponse create(ScheduleRequest dto) {

        GroupEntity group = groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not Found"));

        CourseEntity course = courseRepository.findById(dto.courseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not Found"));

        TeacherEntity teacher = teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not Found"));

        LocalDateTime start = dto.dateTime();
        LocalDateTime end = scheduleCalculator.calculateEnd(start);

        scheduleValidator.validateWorkingHours(start, end);

        long durationMinutes = scheduleCalculator.calculateDurationMinutes();

        scheduleValidator.validateGroupConflict(dto.groupId(), start, end, durationMinutes);
        scheduleValidator.validateTeacherConflict(dto.teacherId(), start, end, durationMinutes);

        ScheduleEntity lesson = ScheduleEntity.builder()
                .dateTime(start)
                .group(group)
                .course(course)
                .teacher(teacher)
                .build();

        scheduleRepository.save(lesson);

        return scheduleMapper.toResponse(lesson);
    }

    @Transactional
    public ScheduleResponse update(Long id, UpdateScheduleRequest dto) {

        ScheduleEntity lesson = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Lesson not found"));

        LocalDateTime start = dto.dateTime();
        LocalDateTime end = scheduleCalculator.calculateEnd(start);

        scheduleValidator.validateWorkingHours(start, end);

        long durationMinutes = scheduleCalculator.calculateDurationMinutes();

        Long groupId = lesson.getGroup().getId();
        Long teacherId = lesson.getTeacher().getId();

        scheduleValidator.validateGroupConflictForUpdate(id, groupId, start, end, durationMinutes);
        scheduleValidator.validateTeacherConflictForUpdate(id, teacherId, start, end, durationMinutes);

        scheduleMapper.updateScheduleFromDto(lesson, dto);

        return scheduleMapper.toResponse(lesson);
    }

    @Transactional
    public void delete(Long id) {

        ScheduleEntity lesson = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Lesson not found"));

        scheduleRepository.delete(lesson);
    }
}
