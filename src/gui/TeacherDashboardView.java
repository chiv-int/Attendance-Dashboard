package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TeacherDashboardView - Main dashboard for teachers with Home and Classes tabs
 */
public class TeacherDashboardView extends JPanel {
    private MainFrame mainFrame;
    private CardLayout contentLayout;
    private JPanel contentPanel;
    private JLabel homeButton;
    private JLabel classesButton;
    
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
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Show home by default
        showHome();
    }

    private void showHome() {
        contentLayout.show(contentPanel, "HOME");
        homeButton.setForeground(Color.WHITE);
        classesButton.setForeground(new Color(150, 150, 150));
    }

    private void showClasses() {
        contentLayout.show(contentPanel, "CLASSES");
        homeButton.setForeground(new Color(150, 150, 150));
        classesButton.setForeground(Color.WHITE);
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

        classesButton = createNavButton("Classes", false);
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
                return getText().equals("Home") ? homeButton == this : classesButton == this;
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
                if (label == classesButton) {
                    label.setForeground(new Color(168, 126, 79));
                } else {
                    label.setForeground(new Color(176, 176, 176));
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (label == homeButton) {
                    showHome();
                } else if (label == classesButton) {
                    showClasses();
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
        homeContentPanel.setBackground(new Color(245, 245, 245));
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

        // Main content card
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(new Color(220, 220, 220));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setOpaque(false);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        cardPanel.setPreferredSize(new Dimension(1100, 300));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Empty state - centered
        JPanel emptyStatePanel = new JPanel();
        emptyStatePanel.setLayout(new BoxLayout(emptyStatePanel, BoxLayout.Y_AXIS));
        emptyStatePanel.setOpaque(false);

        JLabel emptyIcon = new JLabel("📚");
        emptyIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyStatePanel.add(emptyIcon);

        emptyStatePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel emptyText = new JLabel("Content will be added here soon");
        emptyText.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyText.setForeground(new Color(136, 136, 136));
        emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyStatePanel.add(emptyText);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(emptyStatePanel, gbc);

        homeContentPanel.add(cardPanel);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(homeContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(245, 245, 245));

        return scrollPane;
    }

    private JScrollPane createClassesPanel() {
        JPanel classesContentPanel = new JPanel();
        classesContentPanel.setLayout(new BoxLayout(classesContentPanel, BoxLayout.Y_AXIS));
        classesContentPanel.setBackground(new Color(78, 129, 136));
        classesContentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Title
        JLabel classesTitle = new JLabel("Classes");
        classesTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        classesTitle.setForeground(new Color(176, 176, 176));
        classesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        classesContentPanel.add(classesTitle);

        classesContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Class cards
        String[][] classes = {
            {"CS301", "Data Structures & Algorithms"},
            {"CS402", "Machine Learning"},
            {"CS205", "Database Systems"},
            {"CS101", "Introduction to Programming"}
        };

        for (String[] classInfo : classes) {
            JPanel classCard = createStudentStyleClassCard(classInfo[0], classInfo[1]);
            classCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            final String courseCode = classInfo[0];
            final String courseName = classInfo[1];
            classCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mainFrame.showClassDetail(courseName, courseCode);
                }
            });
            classesContentPanel.add(classCard);
            classesContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(classesContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(78, 129, 136));

        return scrollPane;
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
        card.setPreferredSize(new Dimension(1100, 100));
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