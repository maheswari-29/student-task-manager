package com.example.studenttaskmanager.dto;

import com.example.studenttaskmanager.entity.Priority;
import com.example.studenttaskmanager.entity.Status;

import java.time.LocalDate;

/**
 * Data we send back to the client for a task.
 * It carries the student's id and name instead of the whole Student object.
 */
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDate dueDate;
    private Long studentId;
    private String studentName;

    public TaskResponse(Long id, String title, String description, Priority priority,
                        Status status, LocalDate dueDate, Long studentId, String studentName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }
}
