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
    private JButton homeButton;
    private JButton classesButton;
    
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
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 58, 64)); // Slightly lighter dark gray
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Left - Logo
        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);
        
        // Center - Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navPanel.setOpaque(false);
        
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(new Color(52, 58, 64));
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setContentAreaFilled(false);
        homeButton.addActionListener(e -> showHome());
        
        classesButton = new JButton("Classes");
        classesButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        classesButton.setForeground(new Color(150, 150, 150)); // Grayed out initially
        classesButton.setBackground(new Color(52, 58, 64));
        classesButton.setBorderPainted(false);
        classesButton.setFocusPainted(false);
        classesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        classesButton.setContentAreaFilled(false);
        classesButton.addActionListener(e -> showClasses());
        
        navPanel.add(homeButton);
        navPanel.add(classesButton);
        
        headerPanel.add(navPanel, BorderLayout.CENTER);
        
        // Right - Profile
        JLabel pfpLabel = new JLabel("pfp");
        pfpLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pfpLabel.setForeground(Color.WHITE);
        headerPanel.add(pfpLabel, BorderLayout.EAST);
        
        return headerPanel;
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
    
    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(67, 79, 84)); // Teal-gray background
        return homePanel;
    }
    
    private JPanel createClassesPanel() {
        JPanel classesPanel = new JPanel();
        classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.Y_AXIS));
        classesPanel.setBackground(new Color(67, 79, 84)); // Teal-gray background
        classesPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        // Sample classes (these would come from backend)
        String[][] classes = {
            {"CS301", "Data Structures & Algorithms"},
            {"CS402", "Machine Learning"},
            {"CS205", "Database Systems"},
            {"CS101", "Introduction to Programming"}
        };
        
        for (String[] classInfo : classes) {
            JPanel classCard = createClassCard(classInfo[0], classInfo[1]);
            classCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            classesPanel.add(classCard);
            classesPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(classesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(67, 79, 84));
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(67, 79, 84));
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createClassCard(String courseCode, String courseName) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(218, 209, 193)); // Beige color like login
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 175), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(800, 90));
        
        // "Class" title in brown
        JLabel classLabel = new JLabel("Class");
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        classLabel.setForeground(new Color(139, 115, 85)); // Brown color
        classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(classLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // University Name (placeholder for course name)
        JLabel nameLabel = new JLabel("University Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLabel);
        
        // Add click listener
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainFrame.showClassDetail(courseName, courseCode);
            }
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(210, 201, 185));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(218, 209, 193));
            }
        });
        
        return card;
    }
}