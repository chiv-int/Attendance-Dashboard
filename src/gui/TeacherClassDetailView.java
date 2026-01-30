package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TeacherClassDetailView - Shows class details with options for Attendance, Week 1, Week 2, etc.
 */
public class TeacherClassDetailView extends JPanel {
    private MainFrame mainFrame;
    private JLabel classLabel;
    private String currentCourseCode;
    private String currentCourseName;
    
    public TeacherClassDetailView(MainFrame mainFrame) {
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
        
        // "> Class" title
        classLabel = new JLabel("> Class");
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        classLabel.setForeground(Color.WHITE);
        classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(classLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Options cards
        String[] options = {"+ Attendace", "+ Week 1", "+ Week 2", "+ Week 3"};
        boolean[] enabled = {true, false, false, false}; // Only Attendance is enabled
        
        for (int i = 0; i < options.length; i++) {
            JPanel optionCard = createOptionCard(options[i], enabled[i]);
            optionCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(optionCard);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        
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

        // Navigation - Home button
        JLabel homeButton = new JLabel("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 13));
        homeButton.setForeground(Color.WHITE);
        homeButton.setHorizontalAlignment(SwingConstants.CENTER);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setBounds(480, 15, 80, 40);
        homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.backToTeacherDashboard();
            }
        });
        headerPanel.add(homeButton);

        // Navigation - Classes button
        JLabel classesButton = new JLabel("Classes");
        classesButton.setFont(new Font("Arial", Font.BOLD, 13));
        classesButton.setForeground(new Color(150, 150, 150)); // Grayed out
        classesButton.setHorizontalAlignment(SwingConstants.CENTER);
        classesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        classesButton.setBounds(580, 15, 80, 40);
        headerPanel.add(classesButton);

        // User info panel - Fixed bounds
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        userPanel.setOpaque(false);
        userPanel.setBounds(850, 10, 240, 50);

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
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());
        userPanel.add(logoutBtn);

        headerPanel.add(userPanel);

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
    
    private JPanel createOptionCard(String title, boolean enabled) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(218, 209, 193));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setPreferredSize(new Dimension(700, 60));
        
        if (enabled) {
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(139, 115, 85)); // Brown color
        card.add(titleLabel);
        
        // Add click listener only if enabled
        if (enabled && title.contains("Attendace")) {
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mainFrame.showAttendanceOptions(currentCourseName, currentCourseCode);
                }
                
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    card.setBackground(new Color(210, 201, 185));
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    card.setBackground(new Color(218, 209, 193));
                }
            });
        }
        
        return card;
    }
    
    public void setClassInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        classLabel.setText("> Class");
    }
}