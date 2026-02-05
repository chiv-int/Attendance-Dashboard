package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Course;


public class TeacherDashboardView extends JPanel {
    private MainFrame mainFrame;
    private CardLayout contentLayout;
    private JPanel contentPanel;
    private JLabel homeButton;
    private JScrollPane classDetailPanel;
    private JLabel classDetailCourseLabel;
    private String currentCourseName;
    private String currentCourseCode;

    public TeacherDashboardView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); // Dark background

        initComponents();
    }

    private void initComponents() {
        // Header with navigation
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Content area with CardLayout
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(new Color(67, 79, 84)); // Medium dark gray

        // Home panel
        JScrollPane homePanel = createHomePanel(); // Changed from JPanel
        contentPanel.add(homePanel, "HOME");

        // Classes panel
        JScrollPane classesPanel = createClassesPanel(); // Changed from JPanel
        contentPanel.add(classesPanel, "CLASSES");

        // Class detail panel (shown inside dashboard)
        classDetailPanel = createClassDetailPanel();
        contentPanel.add(classDetailPanel, "CLASS_DETAIL");

        add(contentPanel, BorderLayout.CENTER);

        // Show home by default
        showHome();
    }

    private void showHome() {
        contentLayout.show(contentPanel, "HOME");
        homeButton.setForeground(Color.WHITE);
    }

    private void showClasses() {
        contentLayout.show(contentPanel, "CLASSES");
        homeButton.setForeground(new Color(150, 150, 150));
    }

    public void showClassDetail(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        classDetailCourseLabel.setText(courseName);
        contentLayout.show(contentPanel, "CLASS_DETAIL");
        homeButton.setForeground(Color.WHITE);
    }

    public void showLastClassDetail() {
        if (currentCourseName != null && currentCourseCode != null) {
            showClassDetail(currentCourseName, currentCourseCode);
        } else {
            showHome();
        }
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

        // Navigation buttons
        homeButton = createNavButton("Home", true);
        centerSection.add(homeButton);

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
                return homeButton == this;
            }
        };

        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(isActive ? new Color(168, 126, 79) : new Color(176, 176, 176));
        label.setPreferredSize(new Dimension(80, 40));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(new Color(168, 126, 79));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (label == homeButton) {
                    label.setForeground(new Color(168, 126, 79));
                } else {
                    label.setForeground(new Color(176, 176, 176));
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (label == homeButton) {
                    showHome();
                }
            }
        });

        return label;
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

    private JScrollPane createHomePanel() {
        JPanel homeContentPanel = new JPanel();
        homeContentPanel.setLayout(new BoxLayout(homeContentPanel, BoxLayout.Y_AXIS));
        homeContentPanel.setBackground(new Color(78, 129, 136));
        homeContentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

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
        welcomePanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Welcome back, Teacher!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.add(welcomeTitle);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel welcomeText = new JLabel("Manage your classes, track student attendance, and view attendance records.");
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeText.setForeground(new Color(242, 242, 242));
        welcomeText.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.add(welcomeText);

        homeContentPanel.add(welcomePanel);
        homeContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Load real courses from backend and display them
        BackendManager backend = BackendManager.getInstance();
        List<Course> teacherCourses = backend.getCoursesForCurrentUser();

        for (Course course : teacherCourses) {
            JPanel classCard = createStudentStyleClassCard(
                    course.getCourseCode(),
                    course.getCourseName()
            );
            
            // Wrap card in a container to control width
            JPanel cardWrapper = new JPanel();
            cardWrapper.setLayout(new BoxLayout(cardWrapper, BoxLayout.X_AXIS));
            cardWrapper.setOpaque(false);
            cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            cardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardWrapper.add(classCard);
            cardWrapper.add(Box.createHorizontalGlue());

            classCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mainFrame.showClassDetail(
                            course.getCourseName(),
                            course.getCourseCode()
                    );
                }
            });

            homeContentPanel.add(cardWrapper);
            homeContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(homeContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(78, 129, 136));

        return scrollPane;
    }

    // Classes panel - now empty or can be used for other content
    private JScrollPane createClassesPanel() {
        JPanel classesContentPanel = new JPanel();
        classesContentPanel.setLayout(new BoxLayout(classesContentPanel, BoxLayout.Y_AXIS));
        classesContentPanel.setBackground(new Color(78, 129, 136));
        classesContentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        addCourseCards(classesContentPanel);

        JScrollPane scrollPane = new JScrollPane(classesContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(78, 129, 136));

        return scrollPane;
    }

    private void addCourseCards(JPanel container) {
        BackendManager backend = BackendManager.getInstance();
        List<Course> teacherCourses = backend.getCoursesForCurrentUser();

        if (teacherCourses.isEmpty()) {
            JPanel emptyStatePanel = new JPanel();
            emptyStatePanel.setLayout(new BoxLayout(emptyStatePanel, BoxLayout.Y_AXIS));
            emptyStatePanel.setOpaque(false);
            emptyStatePanel.setBorder(new EmptyBorder(100, 0, 0, 0));

            JLabel emptyIcon = new JLabel("📚");
            emptyIcon.setFont(new Font("Arial", Font.PLAIN, 48));
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyStatePanel.add(emptyIcon);

            emptyStatePanel.add(Box.createRigidArea(new Dimension(0, 20)));

            JLabel emptyText = new JLabel("No classes available");
            emptyText.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyText.setForeground(new Color(218, 209, 193));
            emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyStatePanel.add(emptyText);

            container.add(emptyStatePanel);
            return;
        }

        for (Course course : teacherCourses) {
            JPanel classCard = createStudentStyleClassCard(
                    course.getCourseCode(),
                    course.getCourseName()
            );
            
            // Wrap card in a container to control width
            JPanel cardWrapper = new JPanel();
            cardWrapper.setLayout(new BoxLayout(cardWrapper, BoxLayout.X_AXIS));
            cardWrapper.setOpaque(false);
            cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            cardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardWrapper.add(classCard);
            cardWrapper.add(Box.createHorizontalGlue());

            classCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mainFrame.showClassDetail(
                            course.getCourseName(),
                            course.getCourseCode()
                    );
                }
            });

            container.add(cardWrapper);
            container.add(Box.createRigidArea(new Dimension(0, 20)));
        }
    }

    private JScrollPane createClassDetailPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(67, 79, 84));
        
        // Responsive padding: scales with window width
        int horizontalPadding = Math.min(60, Math.max(20, (int)(contentPanel.getWidth() * 0.1)));
        contentPanel.setBorder(new EmptyBorder(10, horizontalPadding, 20, horizontalPadding));
        contentPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        breadcrumbPanel.setOpaque(false);
        breadcrumbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel homeLink = createBreadcrumbLink("Home", this::showHome);
        breadcrumbPanel.add(homeLink);
        breadcrumbPanel.add(createBreadcrumbSeparator());

        classDetailCourseLabel = createBreadcrumbText("Class");
        breadcrumbPanel.add(classDetailCourseLabel);

        contentPanel.add(breadcrumbPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JPanel attendanceCard = createOptionCard("Attendance");
        attendanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        attendanceCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (currentCourseName != null && currentCourseCode != null) {
                    mainFrame.showAttendanceOptions(currentCourseName, currentCourseCode);
                }
            }
        });
        contentPanel.add(attendanceCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel lessonsCard = createOptionCard("Lessons");
        lessonsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lessonsCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel exercisesCard = createOptionCard("Exercises");
        exercisesCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(exercisesCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel quizCard = createOptionCard("Quiz");
        quizCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(quizCard);

        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(67, 79, 84));
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
        return scrollPane;
    }

    private JLabel createBreadcrumbLink(String text, Runnable onClick) {
        JLabel label = new JLabel("<html><u>" + text + "</u></html>");
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(new Color(198, 174, 92));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(new Color(208, 184, 102));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(new Color(198, 174, 92));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onClick.run();
            }
        });
        return label;
    }

    private JLabel createBreadcrumbText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JLabel createBreadcrumbSeparator() {
        JLabel label = new JLabel(" > ");
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JPanel createOptionCard(String title) {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        card.setBackground(new Color(218, 209, 193));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 175), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65)); // Responsive width
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, 65)); // Fills available width
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        titleLabel.setForeground(new Color(120, 110, 100));
        card.add(titleLabel);

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

    private JPanel createStudentStyleClassCard(String courseCode, String courseName) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cream background with rounded corners
                g2d.setColor(new Color(245, 242, 233));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Course code
        JLabel codeLabel = new JLabel(courseCode);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        codeLabel.setForeground(new Color(168, 126, 79));
        codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(codeLabel);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Course name
        JLabel nameLabel = new JLabel(courseName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(60, 60, 60));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLabel);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(240, 235, 225));
                card.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 242, 233));
                card.repaint();
            }
        });

        return card;
    }
}