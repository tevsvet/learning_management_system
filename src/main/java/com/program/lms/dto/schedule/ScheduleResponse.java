package com.program.lms.dto.schedule;

import java.time.LocalDateTime;

public record ScheduleResponse(

        Long id,
        LocalDateTime dateTime,
        Long groupId,
        Long courseId,
        Long teacherId
) { }