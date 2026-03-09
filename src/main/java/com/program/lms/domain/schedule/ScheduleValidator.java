package com.program.lms.domain.schedule;

import com.program.lms.config.properties.ScheduleProperties;
import com.program.lms.dao.ScheduleRepository;
import com.program.lms.exception.ScheduleConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduleValidator {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleProperties scheduleProperties;

    public void validateWorkingHours(LocalDateTime start, LocalDateTime end) {

        LocalDateTime dayStart = start.toLocalDate()
                .atTime(scheduleProperties.getStartHour(), 0);

        LocalDateTime dayEnd = start.toLocalDate()
                .atTime(scheduleProperties.getEndHour(), 0);

        if (start.isBefore(dayStart)) {
            throw new ScheduleConflictException("Lesson starts before working hours");
        }

        if (end.isAfter(dayEnd)) {
            throw new ScheduleConflictException("Lesson ends after working hours");
        }
    }

    public void validateGroupConflict(Long groupId,
                                      LocalDateTime start,
                                      LocalDateTime end,
                                      long durationMinutes) {

        if (scheduleRepository.groupHasTimeConflict(groupId, start, end, durationMinutes)) {
            throw new ScheduleConflictException("Group already has a lesson at this time");
        }
    }

    public void validateTeacherConflict(Long teacherId,
                                        LocalDateTime start,
                                        LocalDateTime end,
                                        long durationMinutes) {

        if (scheduleRepository.teacherHasTimeConflict(teacherId, start, end, durationMinutes)) {
            throw new ScheduleConflictException("Teacher already has a lesson at this time");
        }
    }

    public void validateGroupConflictForUpdate(Long lessonId,
                                               Long groupId,
                                               LocalDateTime start,
                                               LocalDateTime end,
                                               long durationMinutes) {

        if (scheduleRepository.groupHasTimeConflictForUpdate(lessonId, groupId, start, end, durationMinutes)) {
            throw new ScheduleConflictException("Group already has a lesson at this time");
        }
    }

    public void validateTeacherConflictForUpdate(Long lessonId,
                                                 Long teacherId,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 long durationMinutes) {

        if (scheduleRepository.teacherHasTimeConflictForUpdate(lessonId, teacherId, start, end, durationMinutes)) {
            throw new ScheduleConflictException("Teacher already has a lesson at this time");
        }
    }
}