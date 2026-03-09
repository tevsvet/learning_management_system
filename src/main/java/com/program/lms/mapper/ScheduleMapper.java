package com.program.lms.mapper;

import com.program.lms.dto.schedule.ScheduleResponse;
import com.program.lms.dto.schedule.UpdateScheduleRequest;
import com.program.lms.mapper.util.StringMapper;
import com.program.lms.model.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = StringMapper.class)
public interface ScheduleMapper {

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "teacherId", source = "teacher.id")
    ScheduleResponse toResponse(ScheduleEntity schedule);

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    void updateScheduleFromDto(@MappingTarget ScheduleEntity schedule, UpdateScheduleRequest dto);
}