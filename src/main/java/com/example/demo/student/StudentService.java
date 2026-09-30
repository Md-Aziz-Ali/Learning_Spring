package com.example.demo.student;

import com.example.demo.common.exception.DuplicateResourceException;
import com.example.demo.student.dto.StudentRequest;
import com.example.demo.student.dto.StudentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("A student with email " + request.email() + " already exists");
        }
        Student student = new Student(request.name(), request.email(), request.age());
        return StudentResponse.from(studentRepository.save(student));
    }
}
