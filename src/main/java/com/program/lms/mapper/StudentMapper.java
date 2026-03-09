package com.program.lms.mapper;

import com.program.lms.dto.student.StudentResponse;
import com.program.lms.dto.student.UpdateStudentRequest;
import com.program.lms.mapper.util.StringMapper;
import com.program.lms.model.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = StringMapper.class)
public interface StudentMapper {

    @Mapping(target = "groupId", source = "group.id")
    StudentResponse toResponse(StudentEntity student);

    @Mapping(target = "group", ignore = true)
    void updateStudentFromDto(@MappingTarget StudentEntity student, UpdateStudentRequest dto);
}