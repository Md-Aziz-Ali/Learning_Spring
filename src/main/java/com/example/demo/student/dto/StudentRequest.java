package com.example.demo.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentRequest(
        @NotBlank(message = "Name must not be empty")
        @Size(max = 50, message = "Name must be at most 50 characters")
        String name,

        @NotBlank(message = "Email must not be empty")
        @Email(message = "Email is not valid")
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email,

        @NotNull(message = "Age is required")
        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 120, message = "Age must be at most 120")
        Integer age
) {
    // Normalize input before validation runs, so "  Aziz  " is checked and stored as "Aziz"
    public StudentRequest {
        name = name == null ? null : name.strip();
        email = email == null ? null : email.strip().toLowerCase();
    }
}
