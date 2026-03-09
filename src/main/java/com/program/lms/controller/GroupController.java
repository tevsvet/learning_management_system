package com.program.lms.controller;

import com.program.lms.dto.group.GroupRequest;
import com.program.lms.dto.group.GroupResponse;
import com.program.lms.dto.page.PageResponse;
import com.program.lms.exception.ApiError;
import com.program.lms.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group", description = "Group management APIs")
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "Get a list of all groups")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List successfully received",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    })
    @GetMapping
    public PageResponse<GroupResponse> getAll(Pageable pageable) {

        return groupService.getAll(pageable);
    }

    @Operation(summary = "Create a new group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group successfully created",
                    content = @Content(schema = @Schema(implementation = GroupResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping
    public ResponseEntity<GroupResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to create a group",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GroupRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "Group A"
                                    }
                                    """))
            )
            @Valid @RequestBody GroupRequest dto) {

        return ResponseEntity.ok(groupService.create(dto));
    }

    @Operation(summary = "Update existing group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group successfully updated",
                    content = @Content(schema = @Schema(implementation = GroupResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> update(
            @Parameter(description = "Group ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to update group",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GroupRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "Updated Group Name"
                                    }
                                    """))
            )
            @Valid @RequestBody GroupRequest dto) {

        return ResponseEntity.ok(groupService.update(id, dto));
    }

    @Operation(summary = "Add students to group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students successfully added"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group or students not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PatchMapping("/{groupId}/students")
    public ResponseEntity<Void> addStudentsToGroup(
            @Parameter(description = "Group ID", example = "1")
            @PathVariable Long groupId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Student ID list",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = """
                                    [1, 2, 3]
                                    """))
            )
            @RequestBody List<Long> studentIds) {

        groupService.addStudentsToGroup(groupId, studentIds);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete existing group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Group successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Group ID", example = "1")
            @PathVariable Long id) {

        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}