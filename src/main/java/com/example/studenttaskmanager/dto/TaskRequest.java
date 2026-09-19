package com.example.studenttaskmanager.dto;

import com.example.studenttaskmanager.entity.Priority;
import com.example.studenttaskmanager.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data the client sends when creating or updating a task.
 * studentId tells us which student the task belongs to.
 */
public class TaskRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 150, message = "Title cannot be longer than 150 characters")
    private String title;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @NotNull(message = "Priority is required and must be LOW, MEDIUM or HIGH")
    private Priority priority;

    @NotNull(message = "Status is required and must be PENDING, IN_PROGRESS or COMPLETED")
    private Status status;

    @NotNull(message = "Due date is required and must be in yyyy-MM-dd format")
    private LocalDate dueDate;

    @NotNull(message = "studentId is required")
    private Long studentId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
