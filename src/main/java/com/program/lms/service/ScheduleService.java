package com.program.lms.service;

import com.program.lms.config.LessonConfig;
import com.program.lms.dao.*;
import com.program.lms.dto.schedule.ScheduleRequest;
import com.program.lms.dto.schedule.ScheduleResponse;
import com.program.lms.dto.schedule.UpdateScheduleRequest;
import com.program.lms.exception.*;
import com.program.lms.mapper.ScheduleMapper;
import com.program.lms.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAll() {

        return scheduleRepository.findAll()
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAllByGroup(Long groupId) {

        groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not Found"));

        return scheduleRepository.findByGroupId(groupId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAllByTeacher(Long teacherId) {

        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not Found"));

        return scheduleRepository.findByTeacherId(teacherId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
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
        LocalDateTime end = start.plus(LessonConfig.LESSON_DURATION);
        long durationMinutes = LessonConfig.LESSON_DURATION.toMinutes();

        if (scheduleRepository.groupHasTimeConflict(
                dto.groupId(), start, end, durationMinutes)) {
            throw new ScheduleConflictException("Group already has a lesson at this time");
        }

        if (scheduleRepository.teacherHasTimeConflict(
                dto.teacherId(), start, end, durationMinutes)) {
            throw new ScheduleConflictException("Teacher already has a lesson at this time");
        }

        ScheduleEntity lesson = ScheduleEntity.builder()
                .dateTime(dto.dateTime())
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
        LocalDateTime end = start.plus(LessonConfig.LESSON_DURATION);
        long durationMinutes = LessonConfig.LESSON_DURATION.toMinutes();

        Long groupId = lesson.getGroup().getId();
        Long teacherId = lesson.getTeacher().getId();

        if (scheduleRepository.groupHasTimeConflictForUpdate(
                id, groupId, start, end, durationMinutes)) {
            throw new ScheduleConflictException("Group already has a lesson at this time");
        }

        if (scheduleRepository.teacherHasTimeConflictForUpdate(
                id, teacherId, start, end, durationMinutes)) {
            throw new ScheduleConflictException("Teacher already has a lesson at this time");
        }

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
