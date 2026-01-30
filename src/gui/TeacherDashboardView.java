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
        JPanel homePanel = createHomePanel();
        contentPanel.add(homePanel, "HOME");
        
        // Classes panel
        JPanel classesPanel = createClassesPanel();
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
        homeButton = createNavButton("Home", true);
        homeButton.setBounds(480, 15, 80, 40);
        headerPanel.add(homeButton);

        classesButton = createNavButton("Classes", false);
        classesButton.setBounds(580, 15, 80, 40);
        headerPanel.add(classesButton);

        // User info panel - Fixed bounds to be visible
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        userPanel.setOpaque(false);
        userPanel.setBounds(850, 10, 230, 50);

        JLabel userNameLabel = new JLabel("Teacher");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setBounds(10, 5, 120, 20);
        userPanel.add(userNameLabel);

        JLabel userRoleLabel = new JLabel("Teacher Account");
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
                return getText().equals("Home") ? homeButton == this : classesButton == this;
            }
        };

        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(isActive ? new Color(168, 126, 79) : new Color(176, 176, 176));
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

    private JPanel createHomePanel() {
        // Wrapper panel for scrolling
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(new Color(245, 245, 245));

        JPanel homeContentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        homeContentPanel.setLayout(null);
        homeContentPanel.setBackground(new Color(245, 245, 245));
        homeContentPanel.setPreferredSize(new Dimension(1100, 600));

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
        welcomePanel.setLayout(null);
        welcomePanel.setBounds(30, 20, 1100, 120);
        welcomePanel.setOpaque(true);

        JLabel welcomeTitle = new JLabel("Welcome back, Teacher!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setBounds(30, 15, 400, 35);
        welcomePanel.add(welcomeTitle);

        JLabel welcomeText = new JLabel("Manage your classes, track student attendance, and view attendance records.");
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeText.setForeground(new Color(242, 242, 242));
        welcomeText.setBounds(30, 55, 600, 50);
        welcomePanel.add(welcomeText);

        homeContentPanel.add(welcomePanel);

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
        cardPanel.setLayout(null);
        cardPanel.setOpaque(false);
        cardPanel.setBounds(30, 160, 1100, 300);

        // Empty state
        JLabel emptyIcon = new JLabel("📚");
        emptyIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        emptyIcon.setBounds(435, 80, 50, 50);
        emptyIcon.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(emptyIcon);

        JLabel emptyText = new JLabel("Content will be added here soon");
        emptyText.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyText.setForeground(new Color(136, 136, 136));
        emptyText.setBounds(50, 150, 840, 50);
        emptyText.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(emptyText);

        homeContentPanel.add(cardPanel);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(homeContentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
    }

    private JPanel createClassesPanel() {
        // Wrapper panel for scrolling
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(new Color(78, 129, 136));

        JPanel classesContentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        classesContentPanel.setLayout(null);
        classesContentPanel.setBackground(new Color(78, 129, 136));
        classesContentPanel.setPreferredSize(new Dimension(1000, 600));

        // Title
        JLabel classesTitle = new JLabel("Classes");
        classesTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        classesTitle.setForeground(new Color(176, 176, 176));
        classesTitle.setBounds(30, 20, 100, 30);
        classesContentPanel.add(classesTitle);

        // Class cards
        String[][] classes = {
            {"CS301", "Data Structures & Algorithms"},
            {"CS402", "Machine Learning"},
            {"CS205", "Database Systems"},
            {"CS101", "Introduction to Programming"}
        };

        int yPos = 70;
        for (String[] classInfo : classes) {
            JPanel classCard = createStudentStyleClassCard(classInfo[0], classInfo[1]);
            classCard.setBounds(30, yPos, 1100, 100);
            classCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final String courseCode = classInfo[0];
            final String courseName = classInfo[1];
            classCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mainFrame.showClassDetail(courseName, courseCode);
                }
            });
            classesContentPanel.add(classCard);
            yPos += 120;
        }

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(classesContentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
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
        card.setLayout(null);
        card.setOpaque(false);

        // Course code
        JLabel codeLabel = new JLabel(courseCode);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        codeLabel.setForeground(new Color(168, 126, 79));
        codeLabel.setBounds(25, 20, 200, 30);
        card.add(codeLabel);

        // Course name
        JLabel nameLabel = new JLabel(courseName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(60, 60, 60));
        nameLabel.setBounds(25, 55, 800, 30);
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