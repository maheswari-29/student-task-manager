package com.example.studenttaskmanager.exception;

/**
 * Thrown when a student or a task with the given id does not exist.
 * It extends RuntimeException so we do not have to write throws everywhere.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
