package models;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private String teacherId;
    private final String employeeId;
    private final String department;
    private final List<String> assignedCourseIds;
    
    public Teacher(String userId, String username, String password,
                   String fullName, String email, String phone, String department) {
        super(userId, username, password, fullName, email, phone);
        this.teacherId = userId;  // For backward compatibility
        this.employeeId = userId;
        this.department = department;
        this.assignedCourseIds = new ArrayList<>();
    }
    

    
    public String getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public List<String> getAssignedCourseIds() {
        return new ArrayList<>(assignedCourseIds);
    }
    
    public List<String> getTeachingCourseIds() {
        return getAssignedCourseIds();
    }
    
    public void assignCourse(String courseId) {
        if (!assignedCourseIds.contains(courseId)) {
            assignedCourseIds.add(courseId);
        }
    }
    
    public void removeCourse(String courseId) {
        assignedCourseIds.remove(courseId);
    }
    
    @Override
    public String getRole() {
        return "Teacher";
    }
    
    @Override
    public String toString() {
        return String.format("Teacher: %s - %s (%s Department)",
                employeeId, fullName, department);
    }
}
