package com.example.demo.semester;

import com.example.demo.department.Department;
import com.example.demo.department.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SemesterService {
    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Transactional
    public void createSemesters(Department department, int totalSemesters) {
        List<Semester> semesterList = IntStream.rangeClosed(1, totalSemesters)
                .mapToObj(number -> new Semester(number, department))
                .toList();

        semesterRepository.saveAll(semesterList);
    }
}
