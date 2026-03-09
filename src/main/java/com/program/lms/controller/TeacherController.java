package com.program.lms.controller;

import com.program.lms.dto.page.PageResponse;
import com.program.lms.dto.teacher.TeacherRequest;
import com.program.lms.dto.teacher.TeacherResponse;
import com.program.lms.dto.teacher.UpdateTeacherRequest;
import com.program.lms.exception.ApiError;
import com.program.lms.service.TeacherService;
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

@Tag(name = "Teacher", description = "Teacher management APIs")
@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "Get a list of all teachers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List successfully received",
                    content = @Content(schema = @Schema(implementation = TeacherResponse.class))
            )
    })
    @GetMapping
    public PageResponse<TeacherResponse> getAll(Pageable pageable) {

        return teacherService.getAll(pageable);
    }

    @Operation(summary = "Create new teacher")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher successfully created",
                    content = @Content(schema = @Schema(implementation = TeacherResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping
    public ResponseEntity<TeacherResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to create a teacher",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TeacherRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "firstName": "First name",
                                      "lastName": "Last name"
                                    }
                                    """))
            )
            @Valid @RequestBody TeacherRequest dto) {

        return ResponseEntity.ok(teacherService.create(dto));
    }

    @Operation(summary = "Update teacher data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher data successfully updated",
                    content = @Content(schema = @Schema(implementation = TeacherResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<TeacherResponse> update(
            @Parameter(description = "Teacher ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to update teacher",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateTeacherRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "firstName": "New first name",
                                      "lastName": "New last name"
                                    }
                                    """))
            )
            @Valid @RequestBody UpdateTeacherRequest dto) {

        return ResponseEntity.ok(teacherService.update(id, dto));
    }

    @Operation(summary = "Delete teacher")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Teacher successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Teacher ID", example = "1")
            @PathVariable Long id) {

        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}