package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TeacherClassDetailView - Shows class details with options for Attendance, Week 1, Week 2, etc.
 */
public class TeacherClassDetailView extends JPanel {
    private MainFrame mainFrame;
    private JLabel classLabel;
    private String currentCourseCode;
    private String currentCourseName;
    
    public TeacherClassDetailView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); // Dark background
        
        initComponents();
    }
    
    private void initComponents() {
        // Header with navigation
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(67, 79, 84)); // Medium dark gray
        contentPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        // "> Class" title
        classLabel = new JLabel("> Class");
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        classLabel.setForeground(Color.WHITE);
        classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(classLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Options cards
        String[] options = {"+ Attendace", "+ Week 1", "+ Week 2", "+ Week 3"};
        boolean[] enabled = {true, false, false, false}; // Only Attendance is enabled
        
        for (int i = 0; i < options.length; i++) {
            JPanel optionCard = createOptionCard(options[i], enabled[i]);
            optionCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(optionCard);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(67, 79, 84));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 58, 64)); // Slightly lighter dark gray
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Left - Logo
        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);
        
        // Center - Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navPanel.setOpaque(false);
        
        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(new Color(52, 58, 64));
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setContentAreaFilled(false);
        homeButton.addActionListener(e -> mainFrame.backToTeacherDashboard());
        
        JButton classesButton = new JButton("Classes");
        classesButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        classesButton.setForeground(new Color(150, 150, 150)); // Grayed out
        classesButton.setBackground(new Color(52, 58, 64));
        classesButton.setBorderPainted(false);
        classesButton.setFocusPainted(false);
        classesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        classesButton.setContentAreaFilled(false);
        
        navPanel.add(homeButton);
        navPanel.add(classesButton);
        
        headerPanel.add(navPanel, BorderLayout.CENTER);
        
        // Right - Profile
        JLabel pfpLabel = new JLabel("pfp");
        pfpLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pfpLabel.setForeground(Color.WHITE);
        headerPanel.add(pfpLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createOptionCard(String title, boolean enabled) {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        card.setBackground(new Color(218, 209, 193)); // Beige color
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 175), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setPreferredSize(new Dimension(600, 60));
        
        if (enabled) {
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(139, 115, 85)); // Brown color
        card.add(titleLabel);
        
        // Add click listener only if enabled
        if (enabled && title.contains("Attendace")) {
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mainFrame.showAttendanceOptions(currentCourseName, currentCourseCode);
                }
                
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    card.setBackground(new Color(210, 201, 185));
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    card.setBackground(new Color(218, 209, 193));
                }
            });
        }
        
        return card;
    }
    
    public void setClassInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        classLabel.setText("> Class");
    }
}