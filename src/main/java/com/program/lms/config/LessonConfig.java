package com.program.lms.config;

import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LessonConfig {

    public static final Duration LESSON_DURATION = Duration.ofMinutes(90);
}
