package com.program.lms.dto.course;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(

        @NotBlank(message = "Name must not be blank")
        String name,

        String description
) { }