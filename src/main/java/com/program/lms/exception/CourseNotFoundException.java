package com.program.lms.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {

        super(message);
    }
}