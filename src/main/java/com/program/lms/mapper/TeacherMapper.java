package com.program.lms.mapper;

import com.program.lms.dto.teacher.TeacherResponse;
import com.program.lms.dto.teacher.UpdateTeacherRequest;
import com.program.lms.mapper.util.StringMapper;
import com.program.lms.model.TeacherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = StringMapper.class)
public interface TeacherMapper {

    TeacherResponse toResponse(TeacherEntity teacher);

    void updateTeacherFromDto(@MappingTarget TeacherEntity teacher, UpdateTeacherRequest dto);
}