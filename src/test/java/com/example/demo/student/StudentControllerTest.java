package com.example.demo.student;

import com.example.demo.common.exception.DuplicateResourceException;
import com.example.demo.student.dto.StudentRequest;
import com.example.demo.student.dto.StudentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createsStudentAndTrimsName() {
        when(studentService.createStudent(new StudentRequest("Aziz", "aziz@example.com", 22)))
                .thenReturn(new StudentResponse(1L, "Aziz", "aziz@example.com", 22));

        var result = mvc.post().uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "  Aziz  ", "email": " Aziz@Example.com ", "age": 22}
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.CREATED);
        assertThat(result).headers().hasValue("Location", "http://localhost/api/students/1");
        assertThat(result).bodyJson().extractingPath("$.id").isEqualTo(1);
        assertThat(result).bodyJson().extractingPath("$.name").isEqualTo("Aziz");
    }

    @Test
    void rejectsInvalidFieldsWithMessagePerField() {
        var result = mvc.post().uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "   ", "email": "not-an-email", "age": 200}
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(result).bodyJson().extractingPath("$.errors.name").isEqualTo("Name must not be empty");
        assertThat(result).bodyJson().extractingPath("$.errors.email").isEqualTo("Email is not valid");
        assertThat(result).bodyJson().extractingPath("$.errors.age").isEqualTo("Age must be at most 120");
        verify(studentService, never()).createStudent(any());
    }

    @Test
    void rejectsMissingAge() {
        var result = mvc.post().uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Aziz", "email": "aziz@example.com"}
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(result).bodyJson().extractingPath("$.errors.age").isEqualTo("Age is required");
    }

    @Test
    void rejectsMalformedJson() {
        var result = mvc.post().uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": ")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(result).bodyJson().extractingPath("$.detail").isEqualTo("Request body is missing or is not valid JSON");
    }

    @Test
    void returnsConflictForDuplicateEmail() {
        when(studentService.createStudent(any()))
                .thenThrow(new DuplicateResourceException("A student with email aziz@example.com already exists"));

        var result = mvc.post().uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Aziz", "email": "aziz@example.com", "age": 22}
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.CONFLICT);
        assertThat(result).bodyJson().extractingPath("$.detail").isEqualTo("A student with email aziz@example.com already exists");
    }
}
