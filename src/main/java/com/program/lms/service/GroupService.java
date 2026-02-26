package com.program.lms.service;

import com.program.lms.dao.GroupRepository;
import com.program.lms.dao.StudentRepository;
import com.program.lms.dto.group.GroupRequest;
import com.program.lms.dto.group.GroupResponse;
import com.program.lms.dto.page.PageResponse;
import com.program.lms.exception.GroupNotFoundException;
import com.program.lms.exception.StudentNotFoundException;
import com.program.lms.mapper.GroupMapper;
import com.program.lms.mapper.PageMapper;
import com.program.lms.model.GroupEntity;
import com.program.lms.model.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;
    private final PageMapper pageMapper;

    @Transactional(readOnly = true)
    public PageResponse<GroupResponse> getAll(Pageable pageable) {

        return pageMapper.toPageResponse(
                groupRepository.findAll(pageable)
                .map(groupMapper::toResponse));
    }

    @Transactional
    public GroupResponse create(GroupRequest dto) {

        GroupEntity group = GroupEntity.builder()
                .name(dto.name())
                .build();

        groupRepository.save(group);

        return groupMapper.toResponse(group);
    }

    @Transactional
    public GroupResponse update(Long id, GroupRequest dto) {

        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        group.setName(dto.name());

        return groupMapper.toResponse(group);
    }

    @Transactional
    public void delete(Long id) {

        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        groupRepository.delete(group);
    }

    @Transactional
    public void addStudentsToGroup(Long groupId, List<Long> studentIds) {

        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        List<StudentEntity> students = studentRepository.findAllById(studentIds);

        if (students.size() != studentIds.size()) {
            throw new StudentNotFoundException("Some students not found");
        }

        students.forEach(s -> s.setGroup(group));

        studentRepository.saveAll(students);
    }
}