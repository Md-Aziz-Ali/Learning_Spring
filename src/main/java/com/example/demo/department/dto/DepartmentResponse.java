package com.example.demo.department.dto;

import com.example.demo.department.Department;

public record DepartmentResponse(Long id, String name, String code, Integer totalSemesters) {
    public static DepartmentResponse from (Department department){
        return new DepartmentResponse(department.getId(), department.getName(), department.getCode(), department.getTotalSemesters());
    }
}
