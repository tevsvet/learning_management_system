package com.program.lms.controller;

import com.program.lms.dto.course.CourseRequest;
import com.program.lms.dto.course.CourseResponse;
import com.program.lms.dto.course.UpdateCourseRequest;

import com.program.lms.exception.ApiError;
import com.program.lms.service.CourseService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Course", description = "Course management APIs")
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Get a list of all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully received",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class))))
    })
    @GetMapping
    public List<CourseResponse> getAll() {

        return courseService.getAll();
    }

    @Operation(summary = "Create new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<CourseResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to create a course",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "Course name",
                                      "description": "Course description"
                                    }
                                    """)))
            @Valid @RequestBody CourseRequest dto) {

        return ResponseEntity.ok(courseService.create(dto));
    }

    @Operation(summary = "Update existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @Parameter(description = "Course ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to update course",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCourseRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "New course name",
                                      "description": "New course description"
                                    }
                                    """)))
            @Valid @RequestBody UpdateCourseRequest dto) {

        return ResponseEntity.ok(courseService.update(id, dto));
    }

    @Operation(summary = "Delete existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID курса", example = "1")
            @PathVariable Long id) {

        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}