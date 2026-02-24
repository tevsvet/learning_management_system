package com.program.lms.dto.course;

import com.program.lms.validation.NotBlankIfPresent;

public record UpdateCourseRequest(

        @NotBlankIfPresent(message = "Name cannot be blank if provided")
        String name,

        String description
) { }