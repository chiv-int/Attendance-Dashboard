# MySQL Database Setup Guide

## Prerequisites

1. **MySQL Server** - Download and install from [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
2. **MySQL JDBC Driver** - Download from [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)

## Step 1: Install MySQL JDBC Driver

### Option A: Download JAR file
1. Download `mysql-connector-java-8.x.x.jar` from the MySQL website
2. Place it in a `lib` folder in your project directory
3. Add to classpath when compiling and running:
   ```powershell
   javac -cp "lib/*;src" -d bin src/**/*.java
   java -cp "bin;lib/*" Main
   ```

### Option B: Using Maven (if you have Maven setup)
Add to `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

## Step 2: Create the Database

1. **Start MySQL Server** (usually runs as a service)

2. **Open MySQL Command Line** or MySQL Workbench

3. **Run the schema script:**
   ```bash
   mysql -u root -p < database/schema.sql
   ```

   OR manually in MySQL Command Line:
   ```sql
   source C:/Users/Admin/Documents/Coding/INTRO_SE/Attendance_Dashboard/database/schema.sql
   ```

## Step 3: Configure Database Connection

Edit `src/util/DatabaseConnection.java` with your MySQL credentials:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_dashboard";
private static final String DB_USER = "root"; // Your MySQL username
private static final String DB_PASSWORD = "your_password"; // Your MySQL password
```

## Step 4: Verify Database Setup

1. Login to MySQL:
   ```bash
   mysql -u root -p
   ```

2. Check if database exists:
   ```sql
   SHOW DATABASES;
   USE attendance_dashboard;
   SHOW TABLES;
   ```

3. Verify sample data:
   ```sql
   SELECT * FROM users;
   SELECT * FROM students;
   SELECT * FROM courses;
   ```

## Database Schema Overview

### Tables Created:

1. **users** - Basic user authentication
2. **students** - Student-specific information
3. **teachers** - Teacher-specific information
4. **courses** - Course details
5. **course_enrollments** - Student enrollments
6. **lessons** - Class sessions
7. **attendance_codes** - Generated attendance codes with time windows
8. **attendance_records** - Individual attendance records
9. **quizzes** - Quiz information
10. **exercises** - Assignment information

## Default Test Accounts

### Students:
- Username: `student1` | Password: `pass123` (Chiv Intera)
- Username: `student2` | Password: `pass123` (Song Phengroth)

### Teacher:
- Username: `teacher1` | Password: `pass123` (Dr. Sarah Williams)

## Troubleshooting

### Error: "Access denied for user"
- Check your MySQL username and password in `DatabaseConnection.java`
- Ensure MySQL server is running

### Error: "Communications link failure"
- Verify MySQL is running on port 3306
- Check firewall settings

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- Download MySQL Connector/J JAR file
- Add to classpath when running the application

### Error: "Unknown database 'attendance_dashboard'"
- Run the schema.sql script to create the database
- Verify database was created: `SHOW DATABASES;`

## How Attendance Codes Work

1. **Teacher generates code**:
   - Code is saved to `attendance_codes` table
   - Includes password, date, start time, and end time
   - Remains in database even after logout

2. **Student marks attendance**:
   - System validates code from `attendance_codes` table
   - Checks if current time is within the window
   - Creates record in `attendance_records` table

3. **Data persists**:
   - All data survives program restart
   - Teacher can close and reopen application
   - Students can still use valid attendance codes

## Next Steps

After database setup, you can:
1. Compile the project with MySQL connector in classpath
2. Run the application
3. All data will be saved to MySQL database
4. Data persists across application restarts
