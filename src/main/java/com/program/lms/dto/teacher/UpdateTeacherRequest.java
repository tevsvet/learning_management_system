package com.program.lms.dto.teacher;

import com.program.lms.validation.NotBlankIfPresent;

public record UpdateTeacherRequest(

        @NotBlankIfPresent(message = "First name cannot be blank if provided")
        String firstName,

        @NotBlankIfPresent(message = "Last name cannot be blank if provided")
        String lastName
) { }