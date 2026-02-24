package com.program.lms.exception;

public record ApiError(
        String code,
        String message
) { }