package com.example.studenttaskmanager.controller;

import com.example.studenttaskmanager.dto.StudentRequest;
import com.example.studenttaskmanager.dto.StudentResponse;
import com.example.studenttaskmanager.dto.TaskResponse;
import com.example.studenttaskmanager.service.StudentService;
import com.example.studenttaskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles every URL that starts with /api/students.
 * @Valid makes Spring run the validation annotations on StudentRequest.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final TaskService taskService;

    public StudentController(StudentService studentService, TaskService taskService) {
        this.studentService = studentService;
        this.taskService = taskService;
    }

    // POST /api/students -> 201 CREATED
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse created = studentService.createStudent(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET /api/students -> 200 OK
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // GET /api/students/{id} -> 200 OK or 404 NOT FOUND
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // PUT /api/students/{id} -> 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id,
                                                         @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    // DELETE /api/students/{id} -> 204 NO CONTENT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/students/{studentId}/tasks -> all tasks of one student
    @GetMapping("/{studentId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksOfStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(taskService.getTasksByStudentId(studentId));
    }
}
