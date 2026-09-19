package com.example.studenttaskmanager.service;

import com.example.studenttaskmanager.dto.TaskRequest;
import com.example.studenttaskmanager.dto.TaskResponse;
import com.example.studenttaskmanager.entity.Priority;
import com.example.studenttaskmanager.entity.Status;
import com.example.studenttaskmanager.entity.Student;
import com.example.studenttaskmanager.entity.Task;
import com.example.studenttaskmanager.exception.ResourceNotFoundException;
import com.example.studenttaskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * All the business logic for tasks lives here.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final StudentService studentService;

    public TaskService(TaskRepository taskRepository, StudentService studentService) {
        this.taskRepository = taskRepository;
        this.studentService = studentService;
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        // Throws 404 if the student does not exist.
        Student student = studentService.findStudentOrThrow(request.getStudentId());

        Task task = new Task();
        copyRequestIntoTask(request, task, student);
        return toResponse(taskRepository.save(task));
    }

    /**
     * Returns all tasks, optionally filtered.
     * status and priority may be null, which simply means "do not filter by it".
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(Status status, Priority priority) {
        List<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        } else {
            tasks = taskRepository.findAll();
        }

        return toResponseList(tasks);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        return toResponse(findTaskOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStudentId(Long studentId) {
        // Confirms the student exists before returning an empty list.
        studentService.findStudentOrThrow(studentId);
        return toResponseList(taskRepository.findByStudentId(studentId));
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = findTaskOrThrow(id);
        Student student = studentService.findStudentOrThrow(request.getStudentId());

        copyRequestIntoTask(request, task, student);
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    /** Used by both create and update so the field copying is written only once. */
    private void copyRequestIntoTask(TaskRequest request, Task task, Student student) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        task.setStudent(student);
    }

    private List<TaskResponse> toResponseList(List<Task> tasks) {
        List<TaskResponse> responses = new ArrayList<>();
        for (Task task : tasks) {
            responses.add(toResponse(task));
        }
        return responses;
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getStudent().getId(),
                task.getStudent().getName()
        );
    }
}
