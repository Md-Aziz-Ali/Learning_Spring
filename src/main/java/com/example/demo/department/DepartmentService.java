package com.example.demo.department;

import com.example.demo.common.exception.DuplicateResourceException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.department.dto.DepartmentRequest;
import com.example.demo.department.dto.DepartmentResponse;
import com.example.demo.semester.SemesterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final SemesterService semesterService;

    public DepartmentService(DepartmentRepository departmentRepository, SemesterService semesterService) {
        this.departmentRepository = departmentRepository;
        this.semesterService = semesterService;
    }

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if(departmentRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("A department with code " + request.code() + " already exists");
        }
        if(departmentRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("A department with name " + request.name() + " already exists");
        }
        Department department = departmentRepository.save(
                new Department(request.name(), request.code(), request.totalSemesters()));
        semesterService.createSemesters(department, request.totalSemesters());
        return DepartmentResponse.from(department);
//        Department department = new Department(request.name(), request.code(), request.totalSemesters());
//        return DepartmentResponse.from(departmentRepository.save(department));
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found"));
    }
}
