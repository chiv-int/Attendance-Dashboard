package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

/**
 * TeacherAttendanceOptionView - Options for attendance management
 * Features: Generate Password, Attendance List
 */
public class TeacherAttendanceOptionView extends JPanel {
    private MainFrame mainFrame;
    private JLabel titleLabel;
    private String currentCourseCode;
    private String currentCourseName;
    
    public TeacherAttendanceOptionView(MainFrame mainFrame) {
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
        
        // "> Class > Attendance" title
        titleLabel = new JLabel("> Class > Attendance");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Option cards
        JPanel passwordCard = createOptionCard("Generate Password");
        passwordCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showGeneratePassword();
            }
        });
        contentPanel.add(passwordCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel listCard = createOptionCard("Attendance List");
        listCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        listCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainFrame.showAttendanceList(currentCourseName, currentCourseCode);
            }
        });
        contentPanel.add(listCard);
        
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
    
    private JPanel createOptionCard(String title) {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        card.setBackground(new Color(218, 209, 193)); // Beige color
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 175), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        card.setPreferredSize(new Dimension(700, 65));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        titleLabel.setForeground(new Color(120, 110, 100)); // Gray-brown color
        card.add(titleLabel);
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(210, 201, 185));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(218, 209, 193));
            }
        });
        
        return card;
    }
    
    private void showGeneratePassword() {
        // Generate random 6-character password
        String password = generateRandomPassword(6);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel messageLabel = new JLabel("Generated Attendance Password:");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(messageLabel, gbc);
        
        gbc.gridy++;
        JTextField passwordField = new JTextField(password);
        passwordField.setFont(new Font("SansSerif", Font.BOLD, 24));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setEditable(false);
        passwordField.setPreferredSize(new Dimension(200, 50));
        passwordField.setBackground(new Color(232, 240, 254));
        panel.add(passwordField, gbc);
        
        gbc.gridy++;
        JLabel instructionLabel = new JLabel("Share this password with students to mark attendance");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instructionLabel.setForeground(new Color(120, 120, 120));
        panel.add(instructionLabel, gbc);
        
        JOptionPane.showMessageDialog(this, panel, "Attendance Password", 
                                      JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
    
    public void setClassInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        titleLabel.setText("> Class > Attendance");
    }
}