package com.program.lms.mapper;

import com.program.lms.dto.course.CourseResponse;
import com.program.lms.dto.course.CourseShortResponse;
import com.program.lms.dto.course.UpdateCourseRequest;
import com.program.lms.mapper.util.StringMapper;
import com.program.lms.model.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = StringMapper.class)
public interface CourseMapper {

    CourseResponse toResponse(CourseEntity course);

    CourseShortResponse toShortResponse(CourseEntity course);

    @Mapping(target = "description",
            source = "description",
            qualifiedByName = "blankToNull")
    void updateCourseFromDto(@MappingTarget CourseEntity member, UpdateCourseRequest request);
}