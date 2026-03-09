package com.program.lms.dto.student;

public record StudentResponse(

        Long id,
        String firstName,
        String lastName,
        Long groupId
) { }