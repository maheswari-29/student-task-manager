package com.example.studenttaskmanager.dto;

/**
 * Data we send back to the client for a student.
 * Sending this instead of the entity avoids the endless
 * Student -> Task -> Student -> Task ... JSON loop.
 */
public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private int totalTasks;

    public StudentResponse(Long id, String name, String email, int totalTasks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.totalTasks = totalTasks;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalTasks() {
        return totalTasks;
    }
}
