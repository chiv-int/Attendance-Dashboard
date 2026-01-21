package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Course class representing an academic course
 */
public class Course {
    private final String courseId;
    private final String courseCode;
    private final String courseName;
    private final String teacherId;
    private final String schedule;
    private final int credits;
    private final List<String> enrolledStudentIds;
    private final List<Lesson> lessons;
    private final List<Quiz> quizzes;

    public Course(String courseId, String courseCode, String courseName,
                  String teacherId, String schedule, int credits) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherId = teacherId;
        this.schedule = schedule;
        this.credits = credits;
        this.enrolledStudentIds = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.quizzes = new ArrayList<>();
    }

    public void enrollStudent(String studentId) {
        if (!enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
        }
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getCredits() {
        return credits;
    }

    public List<String> getEnrolledStudentIds() {
        return new ArrayList<>(enrolledStudentIds);
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    public List<Quiz> getQuizzes() {
        return new ArrayList<>(quizzes);
    }

    public int getEnrolledStudentCount() {
        return enrolledStudentIds.size();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) | Students: %d | Lessons: %d | Quizzes: %d",
                courseCode, courseName, schedule,
                enrolledStudentIds.size(), lessons.size(), quizzes.size());
    }
}