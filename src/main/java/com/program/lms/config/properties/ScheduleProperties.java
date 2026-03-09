package com.program.lms.config.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "schedule")
public class ScheduleProperties {

    @Min(30)
    @Max(240)
    private int lessonDurationMinutes;

    @Min(5)
    @Max(60)
    private int breakDurationMinutes;

    @Min(0)
    @Max(23)
    private int startHour;

    @Min(0)
    @Max(23)
    private int endHour;

    public Duration getLessonDuration() {
        return Duration.ofMinutes(lessonDurationMinutes);
    }

    public Duration getBreakDuration() {
        return Duration.ofMinutes(breakDurationMinutes);
    }
}