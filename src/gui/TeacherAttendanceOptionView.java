package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import models.Course;

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
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(26, 26, 26), getWidth(), 0, new Color(45, 45, 45));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Left section: Logo and school name
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftSection.setOpaque(false);
        
        // Logo
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("C:\\Users\\M\\OneDrive\\Documents\\Year2\\Introduction to Software Engineering\\project\\Attendance-Dashboard\\src\\gui\\Logo.jpg");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("Logo");
        }
        leftSection.add(logoLabel);

        // School name
        JLabel schoolNameLabel = new JLabel("The Valley University");
        schoolNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        schoolNameLabel.setForeground(Color.WHITE);
        leftSection.add(schoolNameLabel);
        
        headerPanel.add(leftSection, BorderLayout.WEST);

        // Center section: Navigation
        JPanel centerSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerSection.setOpaque(false);
        
        // Navigation - Home button
        JLabel homeButton = new JLabel("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 13));
        homeButton.setForeground(Color.WHITE);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.backToTeacherDashboard();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                homeButton.setForeground(new Color(168, 126, 79));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                homeButton.setForeground(Color.WHITE);
            }
        });
        centerSection.add(homeButton);

        // Navigation - Classes button
        JLabel classesButton = new JLabel("Classes");
        classesButton.setFont(new Font("Arial", Font.BOLD, 13));
        classesButton.setForeground(new Color(150, 150, 150)); // Grayed out
        classesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centerSection.add(classesButton);
        
        headerPanel.add(centerSection, BorderLayout.CENTER);

        // Right section: User info panel
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightSection.setOpaque(false);
        
        // User info container
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        
        JLabel userNameLabel = new JLabel("Teacher");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.add(userNameLabel);

        JLabel userRoleLabel = new JLabel("Teacher Account");
        userRoleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        userRoleLabel.setForeground(new Color(136, 136, 136));
        userRoleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfoPanel.add(userRoleLabel);
        
        rightSection.add(userInfoPanel);

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(90, 40));
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setForeground(new Color(168, 126, 79));
        logoutBtn.setBackground(new Color(26, 26, 26));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(168, 126, 79), 1));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());
        rightSection.add(logoutBtn);

        headerPanel.add(rightSection, BorderLayout.EAST);

        return headerPanel;
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                SchoolLoginUI loginFrame = new SchoolLoginUI();
                loginFrame.setVisible(true);
            });
            // Close the current frame
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) topFrame.dispose();
        }
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
    
    // In TeacherAttendanceOptionView.java - Replace showGeneratePassword()

    private void showGeneratePassword() {
        // Create dialog for date/time input
        JDialog inputDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                        "Generate Attendance Code", true);
        inputDialog.setLayout(new BorderLayout());
        inputDialog.setSize(500, 400);
        inputDialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 242, 233));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Date input
        JLabel dateLabel = new JLabel("Date (DD/MM/YYYY):");
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(dateLabel, gbc);
        
        gbc.gridy++;
        JTextField dateField = new JTextField(java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(dateField, gbc);
        
        // Start time input
        gbc.gridy++;
        JLabel startLabel = new JLabel("Start Time (HH:MM):");
        startLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(startLabel, gbc);
        
        gbc.gridy++;
        JTextField startField = new JTextField("09:00");
        startField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        startField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(startField, gbc);
        
        // End time input
        gbc.gridy++;
        JLabel endLabel = new JLabel("End Time (HH:MM):");
        endLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(endLabel, gbc);
        
        gbc.gridy++;
        JTextField endField = new JTextField("10:00");
        endField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        endField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(endField, gbc);
        
        // Buttons
        gbc.gridy++;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 242, 233));
        
        JButton generateBtn = new JButton("Generate");
        generateBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        generateBtn.setBackground(new Color(198, 174, 92));
        generateBtn.setForeground(Color.BLACK);
        generateBtn.setFocusPainted(false);
        generateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateBtn.setPreferredSize(new Dimension(120, 40));
        
        generateBtn.addActionListener(e -> {
            String date = dateField.getText().trim();
            String startTime = startField.getText().trim();
            String endTime = endField.getText().trim();
            
            // Validate inputs
            if (!isValidDate(date)) {
                JOptionPane.showMessageDialog(inputDialog, 
                    "Invalid date format. Use DD/MM/YYYY", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                JOptionPane.showMessageDialog(inputDialog, 
                    "Invalid time format. Use HH:MM (00:00 - 23:59)", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get course from backend
            BackendManager backend = BackendManager.getInstance();
            Course course = backend.getCourseByName(currentCourseName);
            
            if (course == null) {
                JOptionPane.showMessageDialog(inputDialog, 
                    "Course not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generate code via backend
            String password = backend.generateAttendanceCode(
                course.getCourseId(), date, startTime, endTime
            );
            
            inputDialog.dispose();
            
            // Show success with generated code
            showGeneratedCode(password, date, startTime, endTime);
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(190, 190, 190));
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.addActionListener(e -> inputDialog.dispose());
        
        buttonPanel.add(generateBtn);
        buttonPanel.add(cancelBtn);
        contentPanel.add(buttonPanel, gbc);
        
        inputDialog.add(contentPanel);
        inputDialog.setVisible(true);
    }

    private void showGeneratedCode(String password, String date, String startTime, String endTime) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 242, 233));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel messageLabel = new JLabel("Attendance Code Generated!");
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(messageLabel, gbc);
        
        gbc.gridy++;
        JTextField passwordField = new JTextField(password);
        passwordField.setFont(new Font("SansSerif", Font.BOLD, 32));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setEditable(false);
        passwordField.setPreferredSize(new Dimension(250, 60));
        passwordField.setBackground(new Color(232, 240, 254));
        panel.add(passwordField, gbc);
        
        gbc.gridy++;
        JLabel detailsLabel = new JLabel(String.format(
            "<html><center>Date: %s<br>Time: %s - %s</center></html>", 
            date, startTime, endTime
        ));
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detailsLabel.setForeground(new Color(100, 100, 100));
        panel.add(detailsLabel, gbc);
        
        JOptionPane.showMessageDialog(this, panel, "Success", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isValidDate(String date) {
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) return false;
        try {
            String[] parts = date.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return day >= 1 && day <= 31 && month >= 1 && month <= 12 && 
                year >= 2000 && year <= 2100;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        if (!time.matches("\\d{2}:\\d{2}")) return false;
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
        } catch (Exception e) {
            return false;
        }
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