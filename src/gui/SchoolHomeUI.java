package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Course;

public class SchoolHomeUI extends JFrame {
    private JLabel homeLabel;
    private JPanel contentPanel;
    private JScrollPane scrollPane;

    public SchoolHomeUI() {
        setTitle("The Valley University - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = gd.getDisplayMode();
        setSize(displayMode.getWidth(), displayMode.getHeight());
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content area
        JPanel contentAreaPanel = createContent();
        mainPanel.add(contentAreaPanel, BorderLayout.CENTER);

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
        headerPanel.setLayout(new BorderLayout(15, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(0, 80));

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

        homeLabel = createNavButton("Home", true);
        navPanel.add(homeLabel);

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
                return homeLabel == this;
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
                if (label == homeLabel) {
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
            homeLabel.setForeground(new Color(168, 126, 79));
            homeLabel.repaint();
        }
    }

    private JPanel createContent() {
        // Wrapper panel with scroll capability
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(new Color(78, 129, 136));

        // Main scrollable content panel with responsive layout
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(78, 129, 136));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Welcome section
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(78, 129, 136), getWidth(), getHeight(), new Color(61, 104, 116));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Welcome back, Student!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.add(welcomeTitle);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel welcomeText = new JLabel("View your courses, track attendance, and manage your academic progress.");
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeText.setForeground(new Color(242, 242, 242));
        welcomeText.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.add(welcomeText);

        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Get courses from backend
        BackendManager backend = BackendManager.getInstance();
        List<Course> userCourses = backend.getCoursesForCurrentUser();
        
        // If backend returns courses, use those
        if (userCourses != null && !userCourses.isEmpty()) {
            for (Course course : userCourses) {
                JPanel classCard = createResponsiveClassCard(course.getCourseName(), course.getCourseCode() != null ? course.getCourseCode() : "Course");
                
                // Wrap card in a container to control width
                JPanel cardWrapper = new JPanel();
                cardWrapper.setLayout(new BoxLayout(cardWrapper, BoxLayout.X_AXIS));
                cardWrapper.setOpaque(false);
                cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                cardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
                cardWrapper.add(classCard);
                cardWrapper.add(Box.createHorizontalGlue());
                
                final String courseName = course.getCourseName();
                classCard.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        dispose();
                        SchoolCourseDetailUI courseDetailUI = new SchoolCourseDetailUI(courseName);
                        courseDetailUI.setVisible(true);
                    }
                });
                contentPanel.add(cardWrapper);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        } else {
            // Fallback sample courses with codes
            String[][] courses = {
                {"CS101", "Introduction to Software Engineering"},
                {"CS301", "Data Structures & Algorithms"},
                {"CS402", "Machine Learning"},
                {"CS205", "Database Systems"}
            };
            for (String[] courseData : courses) {
                JPanel classCard = createResponsiveClassCard(courseData[1], courseData[0]);
                
                // Wrap card in a container to control width
                JPanel cardWrapper = new JPanel();
                cardWrapper.setLayout(new BoxLayout(cardWrapper, BoxLayout.X_AXIS));
                cardWrapper.setOpaque(false);
                cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                cardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
                cardWrapper.add(classCard);
                cardWrapper.add(Box.createHorizontalGlue());
                
                final String courseName = courseData[1];
                classCard.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        dispose();
                        SchoolCourseDetailUI courseDetailUI = new SchoolCourseDetailUI(courseName);
                        courseDetailUI.setVisible(true);
                    }
                });
                contentPanel.add(cardWrapper);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        // Main scroll pane
        scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setBackground(new Color(78, 129, 136));
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
    }

    private JPanel createResponsiveClassCard(String courseName, String courseCode) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cream background with rounded corners
                g2d.setColor(new Color(245, 242, 233));
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Course code
        JLabel courseCodeLabel = new JLabel(courseCode);
        courseCodeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        courseCodeLabel.setForeground(new Color(168, 126, 79));
        courseCodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(courseCodeLabel);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Course name
        JLabel courseNameLabel = new JLabel(courseName);
        courseNameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        courseNameLabel.setForeground(new Color(60, 60, 60));
        courseNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(courseNameLabel);

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 236, 225));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(245, 242, 233));
                card.repaint();
            }
        });

        return card;
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
        footerPanel.setPreferredSize(new Dimension(0, 60));

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
            SchoolHomeUI frame = new SchoolHomeUI();
            frame.setVisible(true);
        });
    }
}