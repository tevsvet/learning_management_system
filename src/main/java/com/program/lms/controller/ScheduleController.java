package com.program.lms.controller;

import com.program.lms.dto.schedule.ScheduleRequest;
import com.program.lms.dto.schedule.ScheduleResponse;
import com.program.lms.dto.schedule.UpdateScheduleRequest;
import com.program.lms.exception.ApiError;
import com.program.lms.service.ScheduleService;
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

@Tag(name = "Schedule", description = "Schedule management APIs")
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "Get the full schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule successfully received",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ScheduleResponse.class))))
    })
    @GetMapping
    public List<ScheduleResponse> getAll() {

        return scheduleService.getAll();
    }

    @Operation(summary = "Get a schedule by group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule for group successfully received",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ScheduleResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Group not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/group/{groupId}")
    public List<ScheduleResponse> getAllByGroup(
            @Parameter(description = "Group ID", example = "1")
            @PathVariable Long groupId) {

        return scheduleService.getAllByGroup(groupId);
    }

    @Operation(summary = "Get a schedule by teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule for teacher successfully received",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ScheduleResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/teacher/{teacherId}")
    public List<ScheduleResponse> getAllByTeacher(
            @Parameter(description = "Teacher ID", example = "1")
            @PathVariable Long teacherId) {

        return scheduleService.getAllByTeacher(teacherId);
    }

    @Operation(summary = "Create a lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "No group, course or teacher found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Schedule conflict",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<ScheduleResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to create a lesson",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "dateTime": "2026-01-01T10:00",
                                      "groupId": 1,
                                      "courseId": 2,
                                      "teacherId": 3
                                    }
                                    """)))
            @Valid @RequestBody ScheduleRequest dto) {

        return ResponseEntity.ok(scheduleService.create(dto));
    }

    @Operation(summary = "Update existing lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Schedule conflict",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponse> update(
            @Parameter(description = "Schedule ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New date and time",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateScheduleRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "dateTime": "2026-01-01T12:00"
                                    }
                                    """)))
            @Valid @RequestBody UpdateScheduleRequest dto) {

        return ResponseEntity.ok(scheduleService.update(id, dto));
    }

    @Operation(summary = "Delete existing lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lesson successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Schedule ID", example = "1")
            @PathVariable Long id) {

        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}