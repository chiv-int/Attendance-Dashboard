package gui;

import javax.swing.*;

import models.AttendanceRecord;
import models.Course;
import models.Student;

import java.awt.*;
import java.awt.event.*;

public class SchoolAttendanceUI extends JFrame {
    private JLabel homeLabel;
    private JLabel courseLabel;
    private String currentCourseName;
    private JPasswordField passwordField;
    private JRadioButton presentButton;
    private JRadioButton lateButton;

    public static void openAttendance(String courseName) {
        SwingUtilities.invokeLater(() -> {
            SchoolAttendanceUI attendanceFrame = new SchoolAttendanceUI(courseName);
            attendanceFrame.setVisible(true);
        });
    }

    public SchoolAttendanceUI(String courseName) {
        this.currentCourseName = courseName;
        
        setTitle("The Valley University - " + courseName + " > Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = gd.getDisplayMode();
        setSize(displayMode.getWidth(), displayMode.getHeight());
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel with BorderLayout (responsive!)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header (fixed height, responsive width)
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content area (fills remaining space)
        JPanel contentPanel = createContent();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer (fixed height, responsive width)
        JPanel footerPanel = createFooter();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(26, 26, 26), getWidth(), 0, new Color(45, 45, 45));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout(15, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left: Logo + School name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("C:\\Users\\M\\OneDrive\\Documents\\Year2\\Introduction to Software Engineering\\project\\Attendance-Dashboard\\src\\gui\\Logo.jpg");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("🏫");
            logoLabel.setFont(new Font("Arial", Font.PLAIN, 24));
            logoLabel.setForeground(Color.WHITE);
        }
        leftPanel.add(logoLabel);

        JLabel schoolNameLabel = new JLabel("The Valley University");
        schoolNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        schoolNameLabel.setForeground(Color.WHITE);
        leftPanel.add(schoolNameLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Center: Navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        navPanel.setOpaque(false);

        homeLabel = createNavButton("Home", false);
        navPanel.add(homeLabel);

        courseLabel = createNavButton("Course", true);
        navPanel.add(courseLabel);

        headerPanel.add(navPanel, BorderLayout.CENTER);

        // Right: User info + Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        JLabel userNameLabel = new JLabel("Student");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userNameLabel.setForeground(Color.WHITE);
        userInfoPanel.add(userNameLabel);

        JLabel userRoleLabel = new JLabel("Student Account");
        userRoleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        userRoleLabel.setForeground(new Color(136, 136, 136));
        userInfoPanel.add(userRoleLabel);

        rightPanel.add(userInfoPanel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setForeground(new Color(168, 126, 79));
        logoutBtn.setBackground(new Color(26, 26, 26));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(168, 126, 79), 1));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(90, 30));
        logoutBtn.addActionListener(e -> handleLogout());
        rightPanel.add(logoutBtn);

        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JLabel createNavButton(String text, boolean isActive) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (isActive()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(168, 126, 79, 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);

                    g2d.setColor(new Color(168, 126, 79));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);
                }
                super.paintComponent(g);
            }

            private boolean isActive() {
                return getText().equals("Home") ? homeLabel == this : courseLabel == this;
            }
        };

        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(isActive ? new Color(168, 126, 79) : new Color(176, 176, 176));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(80, 40));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(new Color(168, 126, 79));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (label == courseLabel) {
                    label.setForeground(new Color(168, 126, 79));
                } else {
                    label.setForeground(new Color(176, 176, 176));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleNavClick(label);
            }
        });

        return label;
    }

    private void handleNavClick(JLabel clickedLabel) {
        if (clickedLabel.getText().equals("Home")) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                SchoolHomeUI homeFrame = new SchoolHomeUI();
                homeFrame.setVisible(true);
            });
        } else if (clickedLabel.getText().equals("Course")) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                SchoolCourseUI courseFrame = new SchoolCourseUI();
                courseFrame.setVisible(true);
            });
        }
    }

    private JPanel createContent() {
        JPanel wrapperPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(78, 129, 136));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Use GridBagLayout for flexible, responsive layout
        wrapperPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Breadcrumb at top
        JLabel breadcrumb = new JLabel("> " + currentCourseName + " > Attendance");
        breadcrumb.setFont(new Font("Arial", Font.PLAIN, 16));
        breadcrumb.setForeground(new Color(200, 200, 200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 50, 30, 50);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        wrapperPanel.add(breadcrumb, gbc);

        // Form card - centered in remaining space
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 242, 233));
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(30, 60, 30, 60);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(60, 60, 60));
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordLabel, formGbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        passwordField.setPreferredSize(new Dimension(350, 35));
        formGbc.gridx = 1;
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.insets = new Insets(30, 20, 30, 60);
        formPanel.add(passwordField, formGbc);

        // Options label and radio buttons
        JLabel optionsLabel = new JLabel("Options:");
        optionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        optionsLabel.setForeground(new Color(60, 60, 60));
        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formGbc.anchor = GridBagConstraints.EAST;
        formGbc.insets = new Insets(30, 60, 30, 60);
        formPanel.add(optionsLabel, formGbc);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        radioPanel.setOpaque(false);

        presentButton = new JRadioButton("Present");
        presentButton.setFont(new Font("Arial", Font.PLAIN, 13));
        presentButton.setOpaque(false);
        presentButton.setSelected(true);
        radioPanel.add(presentButton);

        lateButton = new JRadioButton("Late");
        lateButton.setFont(new Font("Arial", Font.PLAIN, 13));
        lateButton.setOpaque(false);
        radioPanel.add(lateButton);

        ButtonGroup group = new ButtonGroup();
        group.add(presentButton);
        group.add(lateButton);

        formGbc.gridx = 1;
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.insets = new Insets(30, 20, 30, 60);
        formPanel.add(radioPanel, formGbc);

        // Add form to wrapper with centering
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        wrapperPanel.add(formPanel, gbc);

        // Submit button below form
        JButton submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setBackground(new Color(168, 126, 79));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBorder(BorderFactory.createLineBorder(new Color(168, 126, 79), 1));
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setPreferredSize(new Dimension(140, 50));
        submitBtn.addActionListener(e -> handleSubmit());

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 0, 30, 50);
        wrapperPanel.add(submitBtn, gbc);

        return wrapperPanel;
    }

    // In SchoolAttendanceUI.java - Replace handleSubmit()

private void handleSubmit() {
    String password = new String(passwordField.getPassword());
    
    if (password.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Please enter the attendance password", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Determine status from radio buttons
    AttendanceRecord.AttendanceStatus status = presentButton.isSelected() 
        ? AttendanceRecord.AttendanceStatus.PRESENT 
        : AttendanceRecord.AttendanceStatus.LATE;
    
    // Get backend and current user
    BackendManager backend = BackendManager.getInstance();
    Student student = backend.getCurrentStudent();
    
    if (student == null) {
        JOptionPane.showMessageDialog(this, 
            "No student logged in", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Get course
    Course course = backend.getCourseByName(currentCourseName);
    
    if (course == null) {
        JOptionPane.showMessageDialog(this, 
            "Course not found", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Attempt to mark attendance
    boolean success = backend.markAttendance(
        course.getCourseId(),
        student.getStudentId(),
        status,
        password
    );
    
    if (success) {
        // Show success dialog
        showSuccessDialog();
    } else {
        JOptionPane.showMessageDialog(this, 
            "Failed to mark attendance.\n" +
            "Possible reasons:\n" +
            "- Invalid password\n" +
            "- Outside attendance time window\n" +
            "- Already marked attendance", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void showSuccessDialog() {
    JDialog successDialog = new JDialog(this, "Success", true);
    successDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    successDialog.setSize(500, 300);
    successDialog.setLocationRelativeTo(this);
    successDialog.setUndecorated(true);

    JPanel dialogPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    dialogPanel.setLayout(new GridBagLayout());
    dialogPanel.setOpaque(false);

    // Success card
    JPanel cardPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(245, 242, 233));
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
    };
    cardPanel.setLayout(new GridBagLayout());
    cardPanel.setOpaque(false);
    cardPanel.setPreferredSize(new Dimension(400, 100));

    JLabel successLabel = new JLabel("✓ Attendance Marked Successfully!");
    successLabel.setFont(new Font("Arial", Font.BOLD, 24));
    successLabel.setForeground(new Color(60, 60, 60));

    GridBagConstraints cardGbc = new GridBagConstraints();
    cardPanel.add(successLabel, cardGbc);

    GridBagConstraints mainGbc = new GridBagConstraints();
    mainGbc.gridx = 0;
    mainGbc.gridy = 0;
    dialogPanel.add(cardPanel, mainGbc);

    JLabel continueLabel = new JLabel("Click anywhere to continue");
    continueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    continueLabel.setForeground(new Color(200, 200, 200));
    mainGbc.gridy = 1;
    mainGbc.insets = new Insets(50, 0, 0, 0);
    dialogPanel.add(continueLabel, mainGbc);

    dialogPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            successDialog.dispose();
            navigateToCourseDetail();
        }
    });

    successDialog.add(dialogPanel);
    successDialog.setVisible(true);
}

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(26, 26, 26), getWidth(), getHeight(), new Color(45, 45, 45));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(61, 61, 61));
                g2d.drawLine(0, 0, getWidth(), 0);
            }
        };
        footerPanel.setLayout(new BorderLayout());

        JLabel footerText = new JLabel("© 2024 The Valley University. All rights reserved. | Privacy Policy | Contact Support");
        footerText.setFont(new Font("Arial", Font.PLAIN, 12));
        footerText.setForeground(new Color(136, 136, 136));
        footerText.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        footerPanel.add(footerText, BorderLayout.WEST);

        return footerPanel;
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                SchoolLoginUI loginFrame = new SchoolLoginUI();
                loginFrame.setVisible(true);
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SchoolAttendanceUI frame = new SchoolAttendanceUI("Introduction to Software Engineering");
            frame.setVisible(true);
        });
    }

    private void navigateToCourseDetail() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            SchoolCourseDetailUI courseDetailFrame = new SchoolCourseDetailUI(currentCourseName);
            courseDetailFrame.setVisible(true);
        });
    }
}