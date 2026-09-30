package com.example.demo.semester.dto;

import com.example.demo.department.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SemesterRequest(
        @NotNull(message = "Semester number can't be empty")
        Integer number,

        @NotNull(message = "Department id is required")
        Long departmentId
) {
}
