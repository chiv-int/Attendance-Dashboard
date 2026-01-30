package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * TeacherListView - Shows attendance list with search and filter functionality
 */
public class TeacherListView extends JPanel {
    private MainFrame mainFrame;
    private JLabel titleLabel;
    private String currentCourseCode;
    private String currentCourseName;
    
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    
    public TeacherListView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); // Dark background
        
        initComponents();
    }
    
    private void initComponents() {
        // Header with navigation
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(67, 79, 84)); // Medium dark gray
        contentPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        // Title
        titleLabel = new JLabel("> Class > Attendance > Attendance List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Search and table panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Search panel
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
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

        // User info panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        userPanel.setOpaque(false);
        userPanel.setBounds(getWidth() - 250, 10, 240, 50);

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
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setOpaque(false);
        
        // Search field
        searchField = new JTextField(35);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBackground(new Color(218, 209, 193)); // Beige
        searchField.setForeground(new Color(100, 100, 100));
        searchField.setText("Search student name or ID");
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 175), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Clear placeholder text on focus
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search student name or ID")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search student name or ID");
                    searchField.setForeground(new Color(100, 100, 100));
                }
            }
        });
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });
        
        searchPanel.add(searchField);
        
        // Filter button (yellow-gold color)
        JButton filterButton = new JButton("Filter");
        filterButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        filterButton.setForeground(Color.BLACK);
        filterButton.setBackground(new Color(198, 174, 92)); // Yellow-gold
        filterButton.setPreferredSize(new Dimension(90, 42));
        filterButton.setFocusPainted(false);
        filterButton.setBorderPainted(false);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterButton.addActionListener(e -> showFilterDialog());
        searchPanel.add(filterButton);
        
        return searchPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create table with beige background
        String[] columnNames = {"No.", "Name", "Major", "Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add sample data
        addSampleData();
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        attendanceTable.setRowHeight(40);
        attendanceTable.setBackground(new Color(218, 209, 193)); // Beige
        attendanceTable.setForeground(Color.BLACK);
        attendanceTable.setGridColor(new Color(100, 100, 100));
        attendanceTable.setShowGrid(true);
        attendanceTable.setSelectionBackground(new Color(210, 201, 185));
        attendanceTable.setSelectionForeground(Color.BLACK);
        
        // Table header
        attendanceTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        attendanceTable.getTableHeader().setBackground(new Color(218, 209, 193));
        attendanceTable.getTableHeader().setForeground(Color.BLACK);
        attendanceTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        
        // Set column widths
        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // No.
        attendanceTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        attendanceTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Major
        attendanceTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Date
        attendanceTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        
        // Add table sorter
        sorter = new TableRowSorter<>(tableModel);
        attendanceTable.setRowSorter(sorter);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        scrollPane.getViewport().setBackground(new Color(218, 209, 193));
        
        // "Student list" label above table
        JLabel listLabel = new JLabel("Student list");
        listLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listLabel.setForeground(Color.BLACK);
        listLabel.setBackground(new Color(218, 209, 193));
        listLabel.setOpaque(true);
        listLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(100, 100, 100)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        
        tablePanel.add(listLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private void addSampleData() {
        Object[][] sampleData = {
            {1, "Chiv Intera", "Software Engineering", "2025-01-29", "Present"},
            {2, "Song Phengroth", "Software Engineering", "2025-01-29", "Present"},
            {3, "John Smith", "Computer Science", "2025-01-29", "Absent"},
            {4, "Emma Wilson", "Software Engineering", "2025-01-29", "Present"},
            {5, "Michael Brown", "Computer Science", "2025-01-29", "Present"},
            {6, "Sarah Davis", "Information Systems", "2025-01-29", "Absent"},
            {7, "David Miller", "Software Engineering", "2025-01-29", "Present"},
            {8, "Lisa Anderson", "Computer Science", "2025-01-29", "Present"},
            {9, "James Taylor", "Software Engineering", "2025-01-29", "Present"},
            {10, "Emily White", "Information Systems", "2025-01-29", "Absent"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
    
    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty() || text.equals("Search student name or ID")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
    
    private void showFilterDialog() {
        JDialog filterDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                           "Filter Options", true);
        filterDialog.setLayout(new BorderLayout());
        filterDialog.setSize(400, 300);
        filterDialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Filter by Status:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JCheckBox presentBox = new JCheckBox("Present", true);
        presentBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        presentBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(presentBox);
        
        JCheckBox absentBox = new JCheckBox("Absent", true);
        absentBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        absentBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(absentBox);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        applyButton.setForeground(Color.BLACK);
        applyButton.setBackground(new Color(198, 174, 92)); // Yellow-gold
        applyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyButton.setFocusPainted(false);
        applyButton.setBorderPainted(false);
        applyButton.addActionListener(e -> {
            applyFilter(presentBox.isSelected(), absentBox.isSelected());
            filterDialog.dispose();
        });
        contentPanel.add(applyButton);
        
        filterDialog.add(contentPanel, BorderLayout.CENTER);
        filterDialog.setVisible(true);
    }
    
    private void applyFilter(boolean showPresent, boolean showAbsent) {
        if (showPresent && showAbsent) {
            sorter.setRowFilter(null);
        } else if (showPresent) {
            sorter.setRowFilter(RowFilter.regexFilter("Present", 4));
        } else if (showAbsent) {
            sorter.setRowFilter(RowFilter.regexFilter("Absent", 4));
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("^$", 4)); // Show nothing
        }
    }
    
    public void setClassInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        titleLabel.setText("> Class > Attendance > Attendance List");
    }
}