package com.program.lms.dto.schedule;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateScheduleRequest(

        @NotNull
        LocalDateTime dateTime
) { }