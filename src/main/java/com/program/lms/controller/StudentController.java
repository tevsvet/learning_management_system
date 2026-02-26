package com.program.lms.controller;

import com.program.lms.dto.page.PageResponse;
import com.program.lms.dto.student.StudentRequest;
import com.program.lms.dto.student.StudentResponse;
import com.program.lms.dto.student.UpdateStudentRequest;
import com.program.lms.exception.ApiError;
import com.program.lms.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag(name = "Student", description = "Student management APIs")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Get a list of all students")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List successfully received",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    })
    @GetMapping
    public PageResponse<StudentResponse> getAll(Pageable pageable) {

        return studentService.getAll(pageable);
    }

    @Operation(summary = "Get students by group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students of group successfully received",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping("/group/{groupId}")
    public List<StudentResponse> getAllByGroup(
            @Parameter(description = "Group ID", example = "1")
            @PathVariable Long groupId) {

        return studentService.getAllByGroup(groupId);
    }

    @Operation(summary = "Create new student")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student successfully created",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping
    public ResponseEntity<StudentResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to create student",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = StudentRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "firstName": "First name",
                                      "lastName": "Last name",
                                      "groupId": 1
                                    }
                                    """))
            )
            @Valid @RequestBody StudentRequest dto) {

        return ResponseEntity.ok(studentService.create(dto));
    }

    @Operation(summary = "Update student data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student data successfully updated",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student or group not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @Parameter(description = "Student ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to update student",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateStudentRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "firstName": "New first name",
                                      "lastName": "New last name",
                                      "groupId": 2
                                    }
                                    """))
            )
            @Valid @RequestBody UpdateStudentRequest dto) {

        return ResponseEntity.ok(studentService.update(id, dto));
    }

    @Operation(summary = "Delete student")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Student successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Student ID", example = "1")
            @PathVariable Long id) {

        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}