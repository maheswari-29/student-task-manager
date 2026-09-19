# Student Task Manager — Spring Boot REST API

A small REST API that lets you manage students and the tasks assigned to them.
Built as a first Spring Boot project: clean layered architecture, MySQL database,
validation, and proper error handling — without any unnecessary complexity.

---

## 1. Project Overview

The API manages two things:

- **Student** — id, name, email
- **Task** — id, title, description, priority, status, dueDate, and the student it belongs to

Relationship: **One student can have many tasks. Each task belongs to exactly one student.**

Full CRUD is available for both, plus filtering tasks by status and priority,
and listing all tasks of a single student.

---

## 2. Features

- Full CRUD for Students
- Full CRUD for Tasks
- One-to-Many relationship (Student → Tasks) using JPA
- Filter tasks by `status`, by `priority`, or both together
- Get all tasks of one student
- Input validation with Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@NotNull`)
- Duplicate email is rejected
- Global exception handling with clean JSON errors — the app never crashes on a bad request
- Correct HTTP status codes (200, 201, 204, 400, 404, 405)
- DTOs used for request and response, so the database entities are never exposed directly

---

## 3. Technologies

| Area | Technology |
|------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Build tool | Maven |
| Web layer | Spring Web (REST) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| Validation | Spring Boot Starter Validation (Jakarta Bean Validation) |
| Testing tool | Postman |

---

## 4. Architecture

```
Client / Postman
       |
       v
   Controller      <- receives HTTP request, validates input, returns ResponseEntity
       |
       v
    Service        <- business rules, converts Entity <-> DTO, throws exceptions
       |
       v
  Repository       <- Spring Data JPA interfaces (no SQL written by hand)
       |
       v
     MySQL
```

**Why DTOs?**
If we returned the `Student` entity directly, Jackson would follow
Student → tasks → student → tasks ... forever and crash the response.
`StudentResponse` and `TaskResponse` break that loop and also stop the client
from sending fields it should not control (like `id`).

**Why `@Transactional` in the service?**
The task list of a student is loaded lazily. Keeping the mapping inside a
transaction lets Hibernate load it safely when we convert the entity to a DTO.

---

## 5. Project Structure

```
student-task-manager
│
├── pom.xml
├── README.md
├── .gitignore
│
└── src
    ├── main
    │   ├── java
    │   │   └── com/example/studenttaskmanager
    │   │       ├── StudentTaskManagerApplication.java
    │   │       │
    │   │       ├── controller
    │   │       │   ├── StudentController.java
    │   │       │   └── TaskController.java
    │   │       │
    │   │       ├── service
    │   │       │   ├── StudentService.java
    │   │       │   └── TaskService.java
    │   │       │
    │   │       ├── repository
    │   │       │   ├── StudentRepository.java
    │   │       │   └── TaskRepository.java
    │   │       │
    │   │       ├── entity
    │   │       │   ├── Student.java
    │   │       │   ├── Task.java
    │   │       │   ├── Priority.java
    │   │       │   └── Status.java
    │   │       │
    │   │       ├── dto
    │   │       │   ├── StudentRequest.java
    │   │       │   ├── StudentResponse.java
    │   │       │   ├── TaskRequest.java
    │   │       │   └── TaskResponse.java
    │   │       │
    │   │       └── exception
    │   │           ├── ResourceNotFoundException.java
    │   │           ├── ErrorResponse.java
    │   │           └── GlobalExceptionHandler.java
    │   │
    │   └── resources
    │       └── application.properties
    │
    └── test
```

---

## 6. Database Setup

Open MySQL Workbench or the MySQL command line and run:

```sql
CREATE DATABASE student_task_manager;
```

That is all you need to do. **Do not create the tables yourself.**
Hibernate creates `students` and `tasks` automatically the first time you run the app,
because of `spring.jpa.hibernate.ddl-auto=update`.

The tables it creates look like this:

```sql
students
---------------------------------
id       BIGINT  PRIMARY KEY AUTO_INCREMENT
name     VARCHAR(100)  NOT NULL
email    VARCHAR(150)  NOT NULL UNIQUE

tasks
---------------------------------
id          BIGINT  PRIMARY KEY AUTO_INCREMENT
title       VARCHAR(150)  NOT NULL
description VARCHAR(500)
priority    VARCHAR(20)   NOT NULL
status      VARCHAR(20)   NOT NULL
due_date    DATE          NOT NULL
student_id  BIGINT        NOT NULL  (FOREIGN KEY -> students.id)
```

---

## 7. What You Must Change Before Running

Open `src/main/resources/application.properties` and change **two lines**:

```properties
spring.datasource.username=root          # <- your MySQL username
spring.datasource.password=YOUR_PASSWORD # <- your real MySQL password
```

Everything else can stay as it is.

---

## 8. How To Run

### Using IntelliJ IDEA
1. `File → Open` and select the `student-task-manager` folder (the one containing `pom.xml`).
2. Wait for Maven to download the dependencies.
3. Create the MySQL database (section 6) and set your password (section 7).
4. Open `StudentTaskManagerApplication.java` and click the green ▶ Run button.

### Using VS Code
1. Install the "Extension Pack for Java" and "Spring Boot Extension Pack".
2. Open the `student-task-manager` folder.
3. Do steps 3 and 4 from above.

### Using the terminal
```bash
cd student-task-manager
mvn spring-boot:run
```

The server starts at **http://localhost:8080**

---

## 9. API Documentation

Base URL: `http://localhost:8080`

### Student APIs

#### Create Student
| | |
|---|---|
| Method | `POST` |
| URL | `/api/students` |
| Purpose | Add a new student |
| Success | `201 CREATED` |
| Errors | `400` invalid or duplicate email |

Request:
```json
{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```
Response:
```json
{
  "id": 1,
  "name": "Rahul",
  "email": "rahul@gmail.com",
  "totalTasks": 0
}
```

#### Get All Students
| | |
|---|---|
| Method | `GET` |
| URL | `/api/students` |
| Purpose | List every student |
| Success | `200 OK` |

Response:
```json
[
  { "id": 1, "name": "Rahul", "email": "rahul@gmail.com", "totalTasks": 2 },
  { "id": 2, "name": "Priya", "email": "priya@gmail.com", "totalTasks": 0 }
]
```

#### Get Student By ID
| | |
|---|---|
| Method | `GET` |
| URL | `/api/students/{id}` |
| Purpose | Get one student |
| Success | `200 OK` |
| Errors | `404` student not found |

Error response:
```json
{
  "message": "Student not found with id: 10",
  "status": 404,
  "timestamp": "2026-09-17T10:15:30.123"
}
```

#### Update Student
| | |
|---|---|
| Method | `PUT` |
| URL | `/api/students/{id}` |
| Purpose | Change a student's name or email |
| Success | `200 OK` |
| Errors | `400`, `404` |

Request:
```json
{
  "name": "Rahul Kumar",
  "email": "rahul.kumar@gmail.com"
}
```

#### Delete Student
| | |
|---|---|
| Method | `DELETE` |
| URL | `/api/students/{id}` |
| Purpose | Delete a student **and all of his tasks** |
| Success | `204 NO CONTENT` (empty body) |
| Errors | `404` |

#### Get Tasks Of A Student
| | |
|---|---|
| Method | `GET` |
| URL | `/api/students/{studentId}/tasks` |
| Purpose | All tasks belonging to one student |
| Success | `200 OK` |
| Errors | `404` student not found |

---

### Task APIs

#### Create Task
| | |
|---|---|
| Method | `POST` |
| URL | `/api/tasks` |
| Purpose | Create a task for a student |
| Success | `201 CREATED` |
| Errors | `400` invalid body, `404` student not found |

Request:
```json
{
  "title": "Complete Spring Boot Project",
  "description": "Finish the Student Task Manager",
  "priority": "HIGH",
  "status": "PENDING",
  "dueDate": "2026-09-25",
  "studentId": 1
}
```
Response:
```json
{
  "id": 1,
  "title": "Complete Spring Boot Project",
  "description": "Finish the Student Task Manager",
  "priority": "HIGH",
  "status": "PENDING",
  "dueDate": "2026-09-25",
  "studentId": 1,
  "studentName": "Rahul"
}
```

#### Get All Tasks (with optional filters)
| | |
|---|---|
| Method | `GET` |
| URL | `/api/tasks` |
| Purpose | List tasks, optionally filtered |
| Success | `200 OK` |
| Errors | `400` if a filter value is not a valid enum |

Examples:
```http
GET /api/tasks
GET /api/tasks?status=COMPLETED
GET /api/tasks?priority=HIGH
GET /api/tasks?status=PENDING&priority=HIGH
```
Filter values must be written in **capital letters** exactly as the enums are defined.

#### Get Task By ID
| | |
|---|---|
| Method | `GET` |
| URL | `/api/tasks/{id}` |
| Success | `200 OK` |
| Errors | `404` task not found |

#### Update Task
| | |
|---|---|
| Method | `PUT` |
| URL | `/api/tasks/{id}` |
| Purpose | Update a task (also used to mark it COMPLETED) |
| Success | `200 OK` |
| Errors | `400`, `404` |

Send the **full** task body, with the changed values:
```json
{
  "title": "Complete Spring Boot Project",
  "description": "Finished and tested with Postman",
  "priority": "HIGH",
  "status": "COMPLETED",
  "dueDate": "2026-09-25",
  "studentId": 1
}
```

#### Delete Task
| | |
|---|---|
| Method | `DELETE` |
| URL | `/api/tasks/{id}` |
| Success | `204 NO CONTENT` |
| Errors | `404` |

---

### Validation Error Format

If you send `{"name": "", "email": "abc"}` to `POST /api/students`:

```json
{
  "message": "Validation failed",
  "status": 400,
  "timestamp": "2026-09-17T10:15:30.123",
  "errors": {
    "name": "Name cannot be blank",
    "email": "Email must be a valid email address"
  }
}
```

---

## 10. Postman Testing Guide

In Postman, for every POST and PUT request:
`Body → raw → JSON` (this sets `Content-Type: application/json` automatically).

| # | Step | Request | Expect |
|---|------|---------|--------|
| 1 | Start the Spring Boot app | — | Console shows `Started StudentTaskManagerApplication` |
| 2 | Create student | `POST /api/students` with Rahul's JSON | `201`, note the returned `id` |
| 3 | Create a second student | `POST /api/students` with a different email | `201` |
| 4 | Duplicate email test | `POST /api/students` with Rahul's same email | `400` "Email already exists" |
| 5 | Validation test | `POST /api/students` with `{"name":"","email":"abc"}` | `400` with the `errors` object |
| 6 | Get all students | `GET /api/students` | `200`, list of 2 |
| 7 | Get student by id | `GET /api/students/1` | `200` |
| 8 | Not found test | `GET /api/students/999` | `404` |
| 9 | Create task | `POST /api/tasks` with `studentId: 1` | `201` |
| 10 | Create 2–3 more tasks | different priority and status values | `201` each |
| 11 | Bad student id | `POST /api/tasks` with `studentId: 999` | `404` |
| 12 | Get all tasks | `GET /api/tasks` | `200` |
| 13 | Get task by id | `GET /api/tasks/1` | `200` |
| 14 | Tasks of a student | `GET /api/students/1/tasks` | `200`, only Rahul's tasks |
| 15 | Update task | `PUT /api/tasks/1` changing the description | `200` |
| 16 | Mark completed | `PUT /api/tasks/1` with `"status": "COMPLETED"` | `200`, status is COMPLETED |
| 17 | Filter by status | `GET /api/tasks?status=COMPLETED` | `200`, only task 1 |
| 18 | Filter by priority | `GET /api/tasks?priority=HIGH` | `200` |
| 19 | Both filters | `GET /api/tasks?status=PENDING&priority=HIGH` | `200` |
| 20 | Invalid filter | `GET /api/tasks?status=DONE` | `400` |
| 21 | Delete task | `DELETE /api/tasks/2` | `204`, empty body |
| 22 | Confirm deletion | `GET /api/tasks/2` | `404` |
| 23 | Delete student | `DELETE /api/students/1` | `204` |
| 24 | Cascade check | `GET /api/tasks` | Rahul's tasks are gone too |

A ready-to-import Postman collection is included:
**`Student-Task-Manager.postman_collection.json`** → in Postman click `Import` and select the file.

---

## 11. Future Enhancements

- Search tasks by title keyword
- Pagination and sorting on `GET /api/tasks`
- Reject due dates in the past with `@FutureOrPresent`
- `PATCH /api/tasks/{id}/status` to update only the status
- Unit tests for the service layer and integration tests with H2
- Swagger / OpenAPI documentation
- Spring Security with JWT login (version 2)
- A simple React or Thymeleaf frontend

---

## 12. Author

Built as a beginner-friendly Spring Boot learning project.
