package com.example.demo.semester.dto;

import com.example.demo.department.Department;
import com.example.demo.semester.Semester;

public record SemesterResponse(Long id, Integer number, Long departmentId, String departmentCode) {
    public static SemesterResponse from(Semester semester) {
        return new SemesterResponse(
                semester.getId(),
                semester.getNumber(),
                semester.getDepartment().getId(),
                semester.getDepartment().getCode());
    }
}
