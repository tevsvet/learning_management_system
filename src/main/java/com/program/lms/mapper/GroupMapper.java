package com.program.lms.mapper;

import com.program.lms.dto.group.GroupResponse;
import com.program.lms.model.GroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponse toResponse(GroupEntity group);
}