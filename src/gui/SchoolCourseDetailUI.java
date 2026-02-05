package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SchoolCourseDetailUI extends JFrame {
    private JLabel homeLabel;
    private String currentCourseName;
    private JPanel contentPanel;
    private List<ExpandableSection> sections;

    public static void openCourseDetail(String courseName) {
        SwingUtilities.invokeLater(() -> {
            SchoolCourseDetailUI detailFrame = new SchoolCourseDetailUI(courseName);
            detailFrame.setVisible(true);
        });
    }

    public SchoolCourseDetailUI(String courseName) {
        this.currentCourseName = courseName;
        
        setTitle("The Valley University - " + courseName);
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
        contentPanel = createContent();
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
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                SchoolHomeUI homeFrame = new SchoolHomeUI();
                homeFrame.setVisible(true);
            });
        }
    }

    private JPanel createBreadcrumb() {
        JPanel breadcrumbPanel = new JPanel();
        breadcrumbPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        breadcrumbPanel.setOpaque(false);

        // Home breadcrumb (clickable)
        JLabel homeBreadcrumb = new JLabel("Home");
        homeBreadcrumb.setFont(new Font("Arial", Font.PLAIN, 13));
        homeBreadcrumb.setForeground(new Color(100, 180, 200));
        homeBreadcrumb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBreadcrumb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    SchoolHomeUI homeFrame = new SchoolHomeUI();
                    homeFrame.setVisible(true);
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                homeBreadcrumb.setForeground(new Color(70, 160, 190));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                homeBreadcrumb.setForeground(new Color(100, 180, 200));
            }
        });
        breadcrumbPanel.add(homeBreadcrumb);

        // Separator
        JLabel separator1 = new JLabel(" > ");
        separator1.setFont(new Font("Arial", Font.PLAIN, 13));
        separator1.setForeground(new Color(150, 150, 150));
        breadcrumbPanel.add(separator1);

        // Courses breadcrumb (clickable - goes back to home courses section)
        JLabel coursesBreadcrumb = new JLabel("Courses");
        coursesBreadcrumb.setFont(new Font("Arial", Font.PLAIN, 13));
        coursesBreadcrumb.setForeground(new Color(100, 180, 200));
        coursesBreadcrumb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        coursesBreadcrumb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    SchoolHomeUI homeFrame = new SchoolHomeUI();
                    homeFrame.setVisible(true);
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                coursesBreadcrumb.setForeground(new Color(70, 160, 190));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                coursesBreadcrumb.setForeground(new Color(100, 180, 200));
            }
        });
        breadcrumbPanel.add(coursesBreadcrumb);

        // Separator
        JLabel separator2 = new JLabel(" > ");
        separator2.setFont(new Font("Arial", Font.PLAIN, 13));
        separator2.setForeground(new Color(150, 150, 150));
        breadcrumbPanel.add(separator2);

        // Current course name (not clickable)
        JLabel currentCourseBreadcrumb = new JLabel(currentCourseName);
        currentCourseBreadcrumb.setFont(new Font("Arial", Font.BOLD, 13));
        currentCourseBreadcrumb.setForeground(new Color(168, 126, 79));
        breadcrumbPanel.add(currentCourseBreadcrumb);

        return breadcrumbPanel;
    }

    private JPanel createContent() {
        // Wrapper panel for scrolling
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(new Color(78, 129, 136));

        // Main scrollable content panel - use GridBagLayout for better width control
        JPanel contentPanelMain = new JPanel();
        contentPanelMain.setLayout(new GridBagLayout());
        contentPanelMain.setBackground(new Color(78, 129, 136));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 50, 0, 50);

        // Breadcrumb navigation
        JPanel breadcrumbPanel = createBreadcrumb();
        contentPanelMain.add(breadcrumbPanel, gbc);

        // Course name header
        JLabel courseNameHeader = new JLabel("> " + currentCourseName);
        courseNameHeader.setFont(new Font("Arial", Font.PLAIN, 18));
        courseNameHeader.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        contentPanelMain.add(courseNameHeader, gbc);

        // Create expandable sections
        sections = new ArrayList<>();
        String[] sectionTitles = {"Attendance", "Week 1", "Week 2", "Week 3"};
        
        int rowIndex = 2;
        for (String title : sectionTitles) {
            gbc.insets = new Insets(30, 50, 0, 50);
            gbc.gridy = rowIndex;
            
            ExpandableSection section = new ExpandableSection(title, currentCourseName);
            section.setPreferredSize(new Dimension(0, 80));
            contentPanelMain.add(section, gbc);
            sections.add(section);
            rowIndex++;
        }

        // Add glue at bottom to push content to top
        gbc.gridy = rowIndex;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanelMain.add(Box.createVerticalGlue(), gbc);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanelMain, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setBackground(new Color(78, 129, 136));
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
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

    // Inner class for expandable section
    private class ExpandableSection extends JPanel {
        private String title;
        private String courseName;
        private boolean isExpanded;

        public ExpandableSection(String title, String courseName) {
            this.title = title;
            this.courseName = courseName;
            this.isExpanded = false;
            setLayout(null);
            setOpaque(true);
            setBackground(new Color(245, 242, 233));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (title.equals("Attendance")) {
                        SchoolAttendanceUI.openAttendance(courseName);
                        SchoolCourseDetailUI.this.dispose();
                    } else {
                        isExpanded = !isExpanded;
                        repaint();
                    }
                }
            });

            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Cream background with rounded corners
            g2d.setColor(new Color(245, 242, 233));
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            // Plus icon
            g2d.setColor(new Color(168, 126, 79));
            int iconSize = Math.min(getWidth(), getHeight()) / 8;
            g2d.setFont(new Font("Arial", Font.BOLD, Math.max(20, iconSize)));
            g2d.drawString("+", 20, getHeight() / 2 + 8);

            // Section title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(title, 60, getHeight() / 2 + 8);
        }

        public boolean isExpanded() {
            return isExpanded;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SchoolCourseDetailUI frame = new SchoolCourseDetailUI("Introduction to Software Engineering");
            frame.setVisible(true);
        });
    }
}