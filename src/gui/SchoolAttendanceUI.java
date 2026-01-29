package gui;

import javax.swing.*;
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
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content area
        JPanel contentPanel = createContent();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer
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
        headerPanel.setLayout(null);
        headerPanel.setPreferredSize(new Dimension(0, 70));

        // Logo
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\M\\OneDrive\\Documents\\Year2\\Introduction to Software Engineering\\project\\Attendance-Dashboard\\src\\gui\\Logo.jpg");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        logoLabel.setBounds(20, 15, 40, 40);
        headerPanel.add(logoLabel);

        // School name
        JLabel schoolNameLabel = new JLabel("The Valley University");
        schoolNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        schoolNameLabel.setForeground(Color.WHITE);
        schoolNameLabel.setBounds(70, 15, 250, 40);
        headerPanel.add(schoolNameLabel);

        // Navigation
        homeLabel = createNavButton("Home", false);
        homeLabel.setBounds(480, 15, 80, 40);
        headerPanel.add(homeLabel);

        courseLabel = createNavButton("Course", true);
        courseLabel.setBounds(580, 15, 80, 40);
        headerPanel.add(courseLabel);

        // User info panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        userPanel.setOpaque(false);
        userPanel.setBounds(getWidth() - 250, 10, 240, 50);

        JLabel userNameLabel = new JLabel("Student");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setBounds(10, 5, 120, 20);
        userPanel.add(userNameLabel);

        JLabel userRoleLabel = new JLabel("Student Account");
        userRoleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        userRoleLabel.setForeground(new Color(136, 136, 136));
        userRoleLabel.setBounds(10, 25, 120, 15);
        userPanel.add(userRoleLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(140, 10, 90, 30);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setForeground(new Color(168, 126, 79));
        logoutBtn.setBackground(new Color(26, 26, 26));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(168, 126, 79), 1));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());
        userPanel.add(logoutBtn);

        headerPanel.add(userPanel);

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
        wrapperPanel.setLayout(null);

        // Course header breadcrumb
        JLabel breadcrumb = new JLabel("> " + currentCourseName + " > Attendance");
        breadcrumb.setFont(new Font("Arial", Font.PLAIN, 16));
        breadcrumb.setForeground(new Color(200, 200, 200));
        breadcrumb.setBounds(50, 30, 800, 40);
        wrapperPanel.add(breadcrumb);

        // Form card
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(245, 242, 233));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        formPanel.setLayout(null);
        formPanel.setOpaque(false);
        formPanel.setBounds(200, 100, 800, 250);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(60, 60, 60));
        passwordLabel.setBounds(60, 50, 150, 30);
        formPanel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(250, 50, 350, 35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        formPanel.add(passwordField);

        // Options label
        JLabel optionsLabel = new JLabel("Options:");
        optionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        optionsLabel.setForeground(new Color(60, 60, 60));
        optionsLabel.setBounds(60, 130, 150, 30);
        formPanel.add(optionsLabel);

        // Radio buttons
        presentButton = new JRadioButton("Present");
        presentButton.setBounds(250, 130, 100, 30);
        presentButton.setFont(new Font("Arial", Font.PLAIN, 13));
        presentButton.setOpaque(false);
        presentButton.setSelected(true);
        formPanel.add(presentButton);

        lateButton = new JRadioButton("Late");
        lateButton.setBounds(380, 130, 100, 30);
        lateButton.setFont(new Font("Arial", Font.PLAIN, 13));
        lateButton.setOpaque(false);
        formPanel.add(lateButton);

        // Button group
        ButtonGroup group = new ButtonGroup();
        group.add(presentButton);
        group.add(lateButton);

        wrapperPanel.add(formPanel);

        // Submit button
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(860, 360, 140, 50);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setBackground(new Color(168, 126, 79));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBorder(BorderFactory.createLineBorder(new Color(168, 126, 79), 1));
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> handleSubmit());
        wrapperPanel.add(submitBtn);

        return wrapperPanel;
    }

    private void handleSubmit() {
        String password = new String(passwordField.getPassword());
        String status = presentButton.isSelected() ? "Present" : "Late";

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show success message with custom dialog
        JDialog successDialog = new JDialog(this, "Success", true);
        successDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        successDialog.setSize(500, 300);
        successDialog.setLocationRelativeTo(this);
        successDialog.setResizable(false);

        JPanel dialogPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 200));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        dialogPanel.setLayout(null);
        dialogPanel.setOpaque(false);

        // Success message card
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 242, 233));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setOpaque(false);
        cardPanel.setBounds(50, 80, 400, 100);

        JLabel successLabel = new JLabel("Submission sent!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 24));
        successLabel.setForeground(new Color(60, 60, 60));
        successLabel.setHorizontalAlignment(SwingConstants.CENTER);
        successLabel.setBounds(0, 25, 400, 60);
        cardPanel.add(successLabel);

        dialogPanel.add(cardPanel);

        // Continue label
        JLabel continueLabel = new JLabel("Click anywhere to continue");
        continueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        continueLabel.setForeground(new Color(200, 200, 200));
        continueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        continueLabel.setBounds(0, 220, 500, 10);
        dialogPanel.add(continueLabel);

        // Add mouse listener to close dialog
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

    private void navigateToCourseDetail() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            SchoolCourseDetailUI detailFrame = new SchoolCourseDetailUI(currentCourseName);
            detailFrame.setVisible(true);
        });
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
        footerPanel.setLayout(null);
        footerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel footerText = new JLabel("© 2024 The Valley University. All rights reserved. | Privacy Policy | Contact Support");
        footerText.setFont(new Font("Arial", Font.PLAIN, 12));
        footerText.setForeground(new Color(136, 136, 136));
        footerText.setBounds(30, 15, getWidth() - 60, 30);
        footerPanel.add(footerText);

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
}