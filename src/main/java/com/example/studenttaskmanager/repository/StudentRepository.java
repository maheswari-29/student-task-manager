package com.example.studenttaskmanager.repository;

import com.example.studenttaskmanager.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository already gives us save, findAll, findById, deleteById and more.
 * We only add the extra method we need.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Spring Data builds the query from the method name.
    boolean existsByEmail(String email);
}
