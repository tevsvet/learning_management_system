package com.program.lms.controller;

import com.program.lms.BaseIntegrationTest;
import com.program.lms.dao.TeacherRepository;
import com.program.lms.dto.teacher.TeacherRequest;
import com.program.lms.dto.teacher.TeacherResponse;
import com.program.lms.exception.ApiError;
import com.program.lms.model.TeacherEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherControllerIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TeacherRepository teacherRepository;

    private final RestTemplate rest = new RestTemplate();

    private String baseUrl;

    @BeforeEach
    void setUp() {

        baseUrl = "http://localhost:" + port + "/api/teachers";
        teacherRepository.deleteAll();
        teacherRepository.save(TeacherEntity.builder()
                .firstName("Base")
                .lastName("Teacher")
                .build());
    }

    @AfterEach
    void cleanUp() {

        teacherRepository.deleteAll();
    }

    @Test
    void create_shouldReturnCreatedTeacherAndPersistIt() {

        TeacherRequest request = new TeacherRequest("John", "Doe");

        ResponseEntity<TeacherResponse> response =
                rest.postForEntity(baseUrl, request, TeacherResponse.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().firstName()).isEqualTo("John");
        assertThat(response.getBody().lastName()).isEqualTo("Doe");

        assertThat(teacherRepository.count()).isEqualTo(2);
        TeacherEntity savedTeacher = teacherRepository.findById(response.getBody().id()).orElseThrow();

        assertThat(savedTeacher.getFirstName()).isEqualTo("John");
        assertThat(savedTeacher.getLastName()).isEqualTo("Doe");
    }

    @Test
    void create_shouldReturnBadRequest_whenFirstNameIsBlank() {

        TeacherRequest request = new TeacherRequest("", "Doe");

        String responseBody;

        try {
            rest.postForEntity(baseUrl, request, ApiError.class);
            throw new AssertionError("Expected BAD_REQUEST for blank firstName");
        } catch (HttpClientErrorException.BadRequest ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            responseBody = ex.getResponseBodyAsString();
        }

        assertThat(responseBody).contains("VALIDATION_ERROR");
        assertThat(responseBody).contains("firstName");

        assertThat(teacherRepository.count()).isEqualTo(1);
    }
}
