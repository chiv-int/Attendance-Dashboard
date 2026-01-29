package models;

 import java.util.ArrayList;
import java.util.List;

/**
 * Teacher class representing a teacher user
 * Stores teacher-specific information and teaching courses
 */
public class Teacher extends User {
    private final String employeeId;
    private final String department;
    private final String specialization;
    private final List<String> teachingCourseIds;

    public Teacher(String userId, String username, String password, String fullName,
                   String employeeId, String department, String specialization) {
        super(userId, username, password, UserRole.TEACHER, fullName);
        this.employeeId = employeeId;
        this.department = department;
        this.specialization = specialization;
        this.teachingCourseIds = new ArrayList<>();
    }

    public void assignCourse(String courseId) {
        if (!teachingCourseIds.contains(courseId)) {
            teachingCourseIds.add(courseId);
        }
    }

    public void removeCourse(String courseId) {
        teachingCourseIds.remove(courseId);
    }

    public boolean isTeaching(String courseId) {
        return teachingCourseIds.contains(courseId);
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<String> getTeachingCourseIds() {
        return new ArrayList<>(teachingCourseIds);
    }

    @Override
    public String toString() {
        return String.format("Teacher{id='%s', name='%s', dept='%s', courses=%d}",
                employeeId, getFullName(), department, teachingCourseIds.size());
    }
}