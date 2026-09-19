# Student Task Manager

A beginner-friendly **Spring Boot REST API** for managing students and their tasks. The application provides CRUD operations for students and tasks, task assignment, filtering, validation, exception handling, and MySQL database integration.

## Project Overview

The **Student Task Manager** is a backend REST API designed to help students organize and manage their tasks. Students can be created and managed through the API, while tasks can be assigned to students with details such as priority, status, description, and due date.

The project demonstrates the development of a RESTful backend application using **Spring Boot, Spring Data JPA, Hibernate, and MySQL**.

## Features

* Student CRUD operations
* Task CRUD operations
* Assign tasks to students
* Retrieve all tasks assigned to a student
* Filter tasks by status and priority
* Request validation using Bean Validation
* Duplicate student email validation
* Global exception handling
* DTO-based request and response handling
* Proper HTTP status codes
* MySQL database integration
* JPA entity relationship between Students and Tasks

## Technologies

* **Java**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **Bean Validation**
* **Postman**
* **Git & GitHub**

## Architecture

The project follows a simple layered architecture:

```text
Client (Postman)
       ↓
Controller
       ↓
Service
       ↓
Repository
       ↓
MySQL Database
```

### Layers

* **Controller** – Handles HTTP requests and responses.
* **Service** – Contains the application's business logic.
* **Repository** – Communicates with the database using Spring Data JPA.
* **Entity** – Represents database tables.
* **DTO** – Handles API request and response data.
* **Exception** – Provides centralized error handling.

## Project Structure

```text
src/main/java/
└── com.example.studenttaskmanager/
    ├── controller/
    ├── service/
    ├── repository/
    ├── entity/
    ├── dto/
    └── exception/
```

## API Endpoints

### Student APIs

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| POST   | `/api/students`      | Create a student    |
| GET    | `/api/students`      | Get all students    |
| GET    | `/api/students/{id}` | Get a student by ID |
| PUT    | `/api/students/{id}` | Update a student    |
| DELETE | `/api/students/{id}` | Delete a student    |

### Task APIs

| Method | Endpoint                         | Description             |
| ------ | -------------------------------- | ----------------------- |
| POST   | `/api/tasks`                     | Create a task           |
| GET    | `/api/tasks`                     | Get all tasks           |
| GET    | `/api/tasks/{id}`                | Get a task by ID        |
| GET    | `/api/tasks/student/{studentId}` | Get tasks for a student |
| PUT    | `/api/tasks/{id}`                | Update a task           |
| DELETE | `/api/tasks/{id}`                | Delete a task           |

### Task Filtering

Tasks can also be filtered using query parameters:

```text
GET /api/tasks?status=PENDING
GET /api/tasks?priority=HIGH
GET /api/tasks?status=PENDING&priority=HIGH
```

## Database

The application uses **MySQL** with the following main entities:

```text
Student
   │
   │  One-to-Many
   ↓
Task
```

One student can have multiple tasks, while each task belongs to one student.

Database schema:

```text
student
--------
id
name
email

task
----
id
title
description
priority
status
due_date
student_id
```

## Setup & Run

### 1. Clone the repository

```bash
git clone https://github.com/maheswari-29/student-task-manager.git
```

### 2. Create the database

Create a MySQL database named:

```sql
CREATE DATABASE student_task_manager;
```

### 3. Configure database credentials

Add your local MySQL credentials in `application-local.properties`.

**Do not commit this file to GitHub.**

### 4. Run the application

Open the project in IntelliJ IDEA and run the main Spring Boot application.

The application runs on:

```text
http://localhost:8080
```

5. Test the APIs

Use **Postman** to test the Student and Task REST APIs.

A Postman collection is included in the repository:

```text
Student-Task-Manager.postman_collection.json
```

Future Enhancements

* User authentication and authorization
* Task search and pagination
* Task sorting
* Due-date reminders
* Frontend application
* Deployment to a cloud platform


