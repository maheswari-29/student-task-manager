package com.example.studenttaskmanager.service;

import com.example.studenttaskmanager.dto.StudentRequest;
import com.example.studenttaskmanager.dto.StudentResponse;
import com.example.studenttaskmanager.entity.Student;
import com.example.studenttaskmanager.exception.ResourceNotFoundException;
import com.example.studenttaskmanager.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * All the business logic for students lives here.
 * The controller only receives the request and passes it to this class.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor injection. Spring passes the repository automatically.
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        Student student = new Student(request.getName(), request.getEmail());
        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            responses.add(toResponse(student));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        return toResponse(findStudentOrThrow(id));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudentOrThrow(id);

        // Only check for a duplicate if the email is actually being changed.
        boolean emailChanged = !student.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailChanged && studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = findStudentOrThrow(id);
        // Because of cascade = ALL on the tasks list, the student's tasks are deleted too.
        studentRepository.delete(student);
    }

    /** Used by this class and by TaskService, so it is public. */
    @Transactional(readOnly = true)
    public Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    /** Converts the entity into the object we send back as JSON. */
    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getTasks().size()
        );
    }
}
