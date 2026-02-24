package com.program.lms.dto.schedule;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ScheduleRequest(

        @NotNull(message = "Date and time must not be null")
        LocalDateTime dateTime,

        @NotNull(message = "Group ID must not be null")
        @Positive(message = "Group ID must be positive")
        Long groupId,

        @NotNull(message = "Course ID must not be null")
        @Positive(message = "Course ID must be positive")
        Long courseId,

        @NotNull(message = "Teacher ID must not be null")
        @Positive(message = "Teacher ID must be positive")
        Long teacherId
) { }