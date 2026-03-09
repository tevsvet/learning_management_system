package com.program.lms.dto.student;

import com.program.lms.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Positive;

public record UpdateStudentRequest(

        @NotBlankIfPresent(message = "First name cannot be blank if provided")
        String firstName,

        @NotBlankIfPresent(message = "Last name cannot be blank if provided")
        String lastName,

        @Positive(message = "Group ID must be positive")
        Long groupId
) { }