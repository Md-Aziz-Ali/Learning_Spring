package com.example.demo.student.dto;

import com.example.demo.student.Student;

public record StudentResponse(Long id, String name, String email, Integer age) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getEmail(), student.getAge());
    }
}
