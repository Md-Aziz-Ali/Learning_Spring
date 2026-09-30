package com.example.demo.department.dto;

import jakarta.validation.constraints.*;

public record DepartmentRequest(
        @NotBlank(message = "Department name cannot be empty")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Department code cannot be empty")
        @Size(max = 10, message = "Code must be at most 10 characters")
        String code,

        @NotNull(message = "Total number of semesters cannot be empty")
        @Min(value = 1, message = "Total semesters must be at least 1")
        @Max(value = 12, message = "Total semesters must be at most 12")
        Integer totalSemesters
) {
    public DepartmentRequest {
        name = name == null ? null : name.strip();
        code = code == null ? null : code.strip().toUpperCase();
    }
}
