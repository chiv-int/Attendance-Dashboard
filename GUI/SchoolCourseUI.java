import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchoolCourseUI extends JFrame {
    private JLabel homeLabel;
    private JLabel courseLabel;
    private List<CourseCard> courseCards;

    public SchoolCourseUI() {
        setTitle("The Valley University - Courses");
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
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\M\\OneDrive\\Documents\\Year2\\Introduction to Software Engineering\\project\\Attendance-Dashboard\\GUI\\Logo.jpg");
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
            homeLabel.setForeground(new Color(176, 176, 176));
            courseLabel.setForeground(new Color(168, 126, 79));
            homeLabel.repaint();
            courseLabel.repaint();
        }
    }

    private JPanel createContent() {
        // Wrapper panel for scrolling
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(new Color(78, 129, 136));

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(78, 129, 136));
        contentPanel.setPreferredSize(new Dimension(1000, 600));

        // Title
        JLabel courseTitle = new JLabel("Courses");
        courseTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        courseTitle.setForeground(new Color(176, 176, 176));
        courseTitle.setBounds(30, 20, 100, 30);
        contentPanel.add(courseTitle);

        // Course cards
        courseCards = new ArrayList<>();
        String[] courses = {
            "Introduction to Software Engineering",
            "Data Structures and Algorithms",
            "Web Development Fundamentals",
            "Database Management Systems"
        };
        String lecturer = "Lecturer";

        int yPos = 70;
        for (String courseName : courses) {
            CourseCard card = new CourseCard(courseName, lecturer);
            card.setBounds(30, yPos, 1100, 100);
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SchoolCourseDetailUI courseDetailUI = new SchoolCourseDetailUI(courseName);
                    courseDetailUI.setVisible(true);
                    disposeCourseFrame();
                }
            });
            contentPanel.add(card);
            courseCards.add(card);
            yPos += 120;
        }

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
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

    private void disposeCourseFrame() {
        this.dispose();
    }

    public static void openCourseDetail(String courseName) {
        SwingUtilities.invokeLater(() -> {
            SchoolCourseDetailUI detailFrame = new SchoolCourseDetailUI(courseName);
            detailFrame.setVisible(true);
        });
    }

    // Inner class for course card
    private static class CourseCard extends JPanel {
        private String courseName;
        private String lecturer;

        public CourseCard(String courseName, String lecturer) {
            this.courseName = courseName;
            this.lecturer = lecturer;
            setLayout(null);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Cream background with rounded corners
            g2d.setColor(new Color(245, 242, 233));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            // Course name
            g2d.setColor(new Color(168, 126, 79));
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString(courseName, 25, 35);

            // Lecturer label
            g2d.setColor(new Color(60, 60, 60));
            g2d.setFont(new Font("Arial", Font.PLAIN, 13));
            g2d.drawString(lecturer, 25, 65);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SchoolCourseUI frame = new SchoolCourseUI();
            frame.setVisible(true);
        });
    }
}