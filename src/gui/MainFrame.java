package gui;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame - Entry point for the Attendance Dashboard GUI
 * This class manages navigation between all views
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // View instances
    private LoginView loginView;
    private TeacherDashboardView teacherDashboardView;
    private TeacherClassDetailView teacherClassDetailView;
    private TeacherAttendanceOptionView teacherAttendanceOptionView;
    private TeacherListView teacherListView;
    
    public MainFrame() {
        setTitle("Attendance Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Initialize CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize all views
        initializeViews();
        
        // Add all views to main panel
        mainPanel.add(loginView, "LOGIN");
        mainPanel.add(teacherDashboardView, "TEACHER_DASHBOARD");
        mainPanel.add(teacherClassDetailView, "TEACHER_CLASS_DETAIL");
        mainPanel.add(teacherAttendanceOptionView, "TEACHER_ATTENDANCE_OPTION");
        mainPanel.add(teacherListView, "TEACHER_LIST");
        
        add(mainPanel);
        
        // Show login view first
        showView("LOGIN");
    }
    
    private void initializeViews() {
        loginView = new LoginView(this);
        teacherDashboardView = new TeacherDashboardView(this);
        teacherClassDetailView = new TeacherClassDetailView(this);
        teacherAttendanceOptionView = new TeacherAttendanceOptionView(this);
        teacherListView = new TeacherListView(this);
    }
    
    /**
     * Navigate to a specific view
     */
    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }
    
    /**
     * Handle login success - navigate to appropriate dashboard
     */
    public void handleLoginSuccess(String userType) {
        if ("TEACHER".equals(userType)) {
            showView("TEACHER_DASHBOARD");
        } else if ("STUDENT".equals(userType)) {
            // For your teammate to implement
            JOptionPane.showMessageDialog(this, 
                "Student view not yet implemented.\nYour teammate will add this feature.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Navigate to class detail view
     */
    public void showClassDetail(String className, String courseCode) {
        teacherClassDetailView.setClassInfo(className, courseCode);
        showView("TEACHER_CLASS_DETAIL");
    }
    
    /**
     * Navigate to attendance option view
     */
    public void showAttendanceOptions(String className, String courseCode) {
        teacherAttendanceOptionView.setClassInfo(className, courseCode);
        showView("TEACHER_ATTENDANCE_OPTION");
    }
    
    /**
     * Navigate to attendance list view
     */
    public void showAttendanceList(String className, String courseCode) {
        teacherListView.setClassInfo(className, courseCode);
        showView("TEACHER_LIST");
    }
    
    /**
     * Go back to teacher dashboard
     */
    public void backToTeacherDashboard() {
        showView("TEACHER_DASHBOARD");
    }
    
    /**
     * Go back to class detail from attendance views
     */
    public void backToClassDetail() {
        showView("TEACHER_CLASS_DETAIL");
    }
    
    public static void main(String[] args) {
        // Start the UI on the EDT and guard startup with error handling so "Run" is safe
        SwingUtilities.invokeLater(() -> {
            try {
                // Try to set native look-and-feel (non-fatal)
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception lfEx) {
                    // fallback silently but print to stderr for diagnostics
                    lfEx.printStackTrace();
                }

                // Create main window
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Throwable t) {
                // Show user-friendly error and exit
                t.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                        "Failed to start Attendance Dashboard:\n" + t.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE));
                System.exit(1);
            }
        });
    }
}