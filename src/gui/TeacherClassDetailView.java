package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class TeacherClassDetailView extends JPanel {
    private MainFrame mainFrame;
    private JPanel breadcrumbPanel;
    private JLabel classesLinkLabel;
    private JLabel courseLabel;
    private String currentCourseCode;
    private String currentCourseName;
    private JLabel homeButton;

    public TeacherClassDetailView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54));

        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Outer panel to hold content at top
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(67, 79, 84));

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(67, 79, 84));
        contentPanel.setBorder(new EmptyBorder(0, 60, 0, 60));

        // Title
        breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        breadcrumbPanel.setOpaque(false);
        breadcrumbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        classesLinkLabel = createBreadcrumbLink("Home", () -> mainFrame.backToTeacherDashboard());
        breadcrumbPanel.add(classesLinkLabel);

        JLabel separator = createBreadcrumbSeparator();
        breadcrumbPanel.add(separator);

        courseLabel = createBreadcrumbText("Class");
        breadcrumbPanel.add(courseLabel);

        contentPanel.add(breadcrumbPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Option cards
        JPanel attendanceCard = createOptionCard("Attendance");
        attendanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        attendanceCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainFrame.showAttendanceOptions(currentCourseName, currentCourseCode);
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

        // Add content to NORTH to keep it at top
        outerPanel.add(contentPanel, BorderLayout.NORTH);

        add(outerPanel, BorderLayout.CENTER);
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
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Left section
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftSection.setOpaque(false);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("C:\\Users\\M\\OneDrive\\Documents\\Year2\\Introduction to Software Engineering\\project\\Attendance-Dashboard\\src\\gui\\Logo.jpg");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("Logo");
        }
        leftSection.add(logoLabel);

        JLabel schoolNameLabel = new JLabel("The Valley University");
        schoolNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        schoolNameLabel.setForeground(Color.WHITE);
        leftSection.add(schoolNameLabel);

        headerPanel.add(leftSection, BorderLayout.WEST);

        // Center section
        JPanel centerSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerSection.setOpaque(false);

        homeButton = createNavButton("Home", false);
        centerSection.add(homeButton);

        headerPanel.add(centerSection, BorderLayout.CENTER);

        // Right section
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightSection.setOpaque(false);

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
                return getText().equals("Classes");
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
                label.setForeground(new Color(176, 176, 176));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (label == homeButton) {
                    mainFrame.backToTeacherDashboard();
                }
            }
        });

        return label;
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
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) topFrame.dispose();
        }
    }

    private JPanel createOptionCard(String title) {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        card.setBackground(new Color(218, 209, 193));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 175), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        card.setPreferredSize(new Dimension(700, 65));
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

    // ADDED: Missing method
    public void setClassInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        courseLabel.setText(courseName);
    }
}