package com.example.studenttaskmanager.repository;

import com.example.studenttaskmanager.entity.Priority;
import com.example.studenttaskmanager.entity.Status;
import com.example.studenttaskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Query methods are created automatically from their names.
 * findByStudentId works because Task has a field called "student" which has an "id".
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStudentId(Long studentId);

    List<Task> findByStatus(Status status);

    List<Task> findByPriority(Priority priority);

    List<Task> findByStatusAndPriority(Status status, Priority priority);
}
