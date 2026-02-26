package com.program.lms.domain.schedule;

import com.program.lms.config.properties.ScheduleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduleCalculator {

    private final ScheduleProperties scheduleProperties;

    public LocalDateTime calculateEnd(LocalDateTime start) {
        return start.plus(scheduleProperties.getLessonDuration())
                    .plus(scheduleProperties.getBreakDuration());
    }

    public long calculateDurationMinutes() {
        return scheduleProperties.getLessonDuration()
                .plus(scheduleProperties.getBreakDuration())
                .toMinutes();
    }
}