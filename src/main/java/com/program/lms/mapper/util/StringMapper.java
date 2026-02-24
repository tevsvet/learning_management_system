package com.program.lms.mapper.util;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class StringMapper {
    @Named("blankToNull")
    public String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
