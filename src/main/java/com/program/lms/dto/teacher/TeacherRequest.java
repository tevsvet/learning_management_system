package com.program.lms.dto.teacher;

import jakarta.validation.constraints.NotBlank;

public record TeacherRequest(

        @NotBlank(message = "First name must not be blank")
        String firstName,

        @NotBlank(message = "Last name must not be blank")
        String lastName
) { }