# Single Responsibility Principle Refactoring - Summary

## Overview
The code has been refactored to follow the **Single Responsibility Principle (SRP)**. Each class now has exactly one reason to change, making the code more maintainable, testable, and scalable.

## File Structure

### Models (`src/models/`)
Models represent data entities and are responsible **only** for storing and managing data.

1. **AttendanceStatus.java**
   - **Responsibility**: Define attendance status enum
   - **Methods**: PRESENT, LATE

2. **Lesson.java**
   - **Responsibility**: Represent a course lesson
   - **Properties**: title, content
   - **Methods**: Getters for title and content

3. **Exercise.java**
   - **Responsibility**: Represent a course exercise
   - **Properties**: title, description
   - **Methods**: Getters for title and description

4. **AttendanceRecord.java**
   - **Responsibility**: Store individual attendance records
   - **Properties**: recordNumber, studentName, major, status, timestamp
   - **Methods**: Getters and string formatting

5. **Course.java**
   - **Responsibility**: Store course information
   - **Properties**: courseName, password, attendanceRecords, lessons, exercises
   - **Methods**: Getters/setters for course data, password verification

---

### Services (`src/service/`)
Services handle business logic and operations. Each service has a specific, focused responsibility.

#### 1. **PasswordGenerator.java**
   - **Responsibility**: Generate random passwords
   - **Why separate**: Password generation is a distinct concern that could be reused or replaced
   - **Methods**: 
     - `generateRandomPassword()` - Creates 6-character alphanumeric password

#### 2. **QRCodeGenerator.java**
   - **Responsibility**: Generate QR code data
   - **Why separate**: QR code generation can be enhanced later (e.g., actual QR image generation) without affecting other services
   - **Methods**:
     - `generateQRData(String password)` - Generates QR data from password

#### 3. **CourseRepository.java**
   - **Responsibility**: Manage course storage and retrieval (Data Access Layer)
   - **Why separate**: Centralizes all database/storage operations; can switch storage mechanism without changing other services
   - **Methods**:
     - `createCourse(String courseName)` - Create and store new course
     - `getCourse(String courseName)` - Retrieve a course
     - `getAllCourses()` - Get all courses
     - `courseExists(String courseName)` - Check course existence
     - `deleteCourse(String courseName)` - Remove a course

#### 4. **AttendanceService.java**
   - **Responsibility**: Handle all attendance-related operations
   - **Why separate**: Encapsulates attendance business logic independently
   - **Methods**:
     - `markAttendance(...)` - Record student attendance
     - `viewAttendanceRecords(String courseName)` - Display attendance records

#### 5. **CourseContentService.java**
   - **Responsibility**: Manage lessons and exercises for courses
   - **Why separate**: Course content management is distinct from attendance and course data
   - **Methods**:
     - `addLesson(String courseName, String title, String content)` - Add lesson
     - `addExercise(String courseName, String title, String description)` - Add exercise
     - `viewLessons(String courseName)` - Display lessons
     - `viewExercises(String courseName)` - Display exercises

#### 6. **TeacherService.java**
   - **Responsibility**: Orchestrate teacher-related operations
   - **Why separate**: Serves as a facade/coordinator for teacher functionality
   - **Methods**:
     - `createCourse(String courseName)` - Create new course
     - `generatePassword(String courseName)` - Generate attendance password
     - `generateQR(String courseName)` - Generate QR code
     - `viewStudentList(String courseName)` - View attendance records
     - `addLesson(...)` - Add course lesson
     - `addExercise(...)` - Add course exercise
     - `getCourseRepository()` - Share repository with other services

#### 7. **StudentService.java**
   - **Responsibility**: Orchestrate student-related operations
   - **Why separate**: Serves as a facade for student functionality
   - **Methods**:
     - `markAttendance(...)` - Mark attendance
     - `viewAvailableCourses()` - List available courses
     - `viewLessons(String courseName)` - View lessons
     - `viewExercises(String courseName)` - View exercises

#### 8. **MainProgram.java**
   - **Responsibility**: Entry point and orchestration
   - **Why separate**: Demonstrates how services work together without mixing business logic

---

## Benefits of This Refactoring

### 1. **Testability**
   - Each service can be tested independently
   - Example: Test `PasswordGenerator` without `TeacherService`
   - Easier to mock dependencies

### 2. **Maintainability**
   - Changes to password generation don't affect attendance logic
   - Each class has a clear, single purpose
   - Easier to understand and modify

### 3. **Reusability**
   - `PasswordGenerator` can be used in different contexts
   - `CourseRepository` can be shared across services
   - Services can be composed in different ways

### 4. **Scalability**
   - Easy to add new features (e.g., `EmailService`, `NotificationService`)
   - Can replace implementations (e.g., `QRCodeGenerator` with actual QR library)
   - Clear extension points

### 5. **Flexibility**
   - Easy to switch storage (file → database → cloud)
   - Easy to add authentication layers
   - Easy to integrate external APIs

---

## Before vs After

### Before (Monolithic):
```
Course.java - 149 lines
  - Store course data
  - Store lessons (nested class)
  - Store exercises (nested class)
  - Store attendance records (nested class)
  - Define enum
```

`TeacherService.java` - 120+ lines
  - Create courses
  - Generate passwords
  - Generate QR codes
  - View student lists
  - Add lessons/exercises
  - Random password generation

`StudentService.java` - 70+ lines
  - Mark attendance
  - View courses
  - View lessons
  - View exercises

### After (Separated by Responsibility):
- **5 Model classes** - Clean data entities
- **3 Focused Services** - Business logic
- **2 Utility Services** - Reusable helpers
- **1 Repository** - Data access
- **2 Orchestrators** - Coordinate operations

---

## Dependency Flow

```
MainProgram
    ├─→ TeacherService
    │   ├─→ CourseRepository
    │   ├─→ PasswordGenerator
    │   ├─→ QRCodeGenerator
    │   ├─→ CourseContentService
    │   └─→ AttendanceService
    │
    └─→ StudentService
        ├─→ CourseRepository
        ├─→ AttendanceService
        └─→ CourseContentService
```

---

## How to Use

```java
// Initialize services
TeacherService teacherService = new TeacherService();

// Create course
teacherService.createCourse("Java Programming");

// Generate password
String password = teacherService.generatePassword("Java Programming");

// Generate QR code
teacherService.generateQR("Java Programming");

// Add content
teacherService.addLesson("Java Programming", "Intro", "Content...");

// Student operations
StudentService studentService = 
    new StudentService(teacherService.getCourseRepository());

studentService.viewAvailableCourses();
studentService.markAttendance("Java Programming", password, 
                              "John", "CS", AttendanceStatus.PRESENT);
```

---

## Future Enhancements

With this structure, future features are easy to add:

1. **EmailService** - Send notifications to students
2. **AuthenticationService** - Handle user authentication
3. **ReportService** - Generate attendance reports
4. **NotificationService** - Push notifications
5. **DatabaseService** - Replace in-memory storage
6. **QRImageService** - Generate actual QR images

Each new feature is isolated and won't break existing code!
