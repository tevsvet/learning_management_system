package com.program.lms.dto.group;

import jakarta.validation.constraints.NotBlank;

public record GroupRequest(

        @NotBlank(message = "Name must not be blank")
        String name
) { }