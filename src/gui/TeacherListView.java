package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * TeacherListView - Shows attendance list with search and filter functionality
 * Enhanced with date filtering options and active filter display
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
    
    // Filter state
    private boolean filterPresent = true;
    private boolean filterAbsent = true;
    private String dateFilterType = "All Time"; // "All Time", "Yesterday", "This Week", "This Month", "Custom"
    private LocalDate customStartDate = null;
    private LocalDate customEndDate = null;
    
    // Active filters panel
    private JPanel activeFiltersPanel;
    
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
        
        // Active filters panel
        activeFiltersPanel = createActiveFiltersPanel();
        mainPanel.add(activeFiltersPanel, BorderLayout.CENTER);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(tableWrapper, BorderLayout.SOUTH);
        
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
        
        // Navigation - Home button
        JLabel homeButton = new JLabel("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 13));
        homeButton.setForeground(Color.WHITE);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.backToTeacherDashboard();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                homeButton.setForeground(new Color(168, 126, 79));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                homeButton.setForeground(Color.WHITE);
            }
        });
        centerSection.add(homeButton);

        // Navigation - Classes button
        JLabel classesButton = new JLabel("Classes");
        classesButton.setFont(new Font("Arial", Font.BOLD, 13));
        classesButton.setForeground(new Color(150, 150, 150)); // Grayed out
        classesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            new EmptyBorder(10, 12, 10, 12)
        ));
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search student name or ID")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(new Color(100, 100, 100));
                    searchField.setText("Search student name or ID");
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
    
    private JPanel createActiveFiltersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 50));
        
        // Don't call updateActiveFiltersDisplay() here - activeFiltersPanel not assigned yet!
        // The panel starts empty, which is correct for initial state (no filters active)
        
        return panel;
    }
    
    private void updateActiveFiltersDisplay() {
        activeFiltersPanel.removeAll();
        
        List<String> activeFilters = new ArrayList<>();
        
        // Check date filter
        if (!dateFilterType.equals("All Time")) {
            String dateText = dateFilterType;
            if (dateFilterType.equals("Custom") && customStartDate != null && customEndDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                dateText = customStartDate.format(formatter) + " - " + customEndDate.format(formatter);
            }
            activeFilters.add("Date: " + dateText);
        }
        
        // Check status filter
        if (filterPresent && !filterAbsent) {
            activeFilters.add("Status: Present");
        } else if (!filterPresent && filterAbsent) {
            activeFilters.add("Status: Absent");
        }
        
        if (!activeFilters.isEmpty()) {
            JLabel filterLabel = new JLabel("Active Filters:");
            filterLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            filterLabel.setForeground(new Color(218, 209, 193));
            activeFiltersPanel.add(filterLabel);
            
            for (String filter : activeFilters) {
                activeFiltersPanel.add(createFilterChip(filter));
            }
            
            // Clear all button
            JButton clearAllBtn = createClearAllButton();
            activeFiltersPanel.add(clearAllBtn);
        }
        
        activeFiltersPanel.revalidate();
        activeFiltersPanel.repaint();
    }
    
    private JPanel createFilterChip(String text) {
        JPanel chip = new JPanel(new BorderLayout(8, 0));
        chip.setBackground(new Color(198, 174, 92, 200)); // Semi-transparent yellow-gold
        chip.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(198, 174, 92), 1),
            new EmptyBorder(5, 12, 5, 8)
        ));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Color.BLACK);
        chip.add(label, BorderLayout.CENTER);
        
        // Close button
        JLabel closeBtn = new JLabel("✕");
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setForeground(Color.BLACK);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                removeFilter(text);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeBtn.setForeground(new Color(150, 0, 0));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeBtn.setForeground(Color.BLACK);
            }
        });
        chip.add(closeBtn, BorderLayout.EAST);
        
        return chip;
    }
    
    private JButton createClearAllButton() {
        JButton btn = new JButton("Clear All");
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(new Color(80, 80, 80));
        btn.setBorder(new EmptyBorder(5, 10, 5, 10));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> clearAllFilters());
        return btn;
    }
    
    private void removeFilter(String filterText) {
        if (filterText.startsWith("Date:")) {
            dateFilterType = "All Time";
            customStartDate = null;
            customEndDate = null;
        } else if (filterText.startsWith("Status:")) {
            filterPresent = true;
            filterAbsent = true;
        }
        updateActiveFiltersDisplay();
        applyAllFilters();
    }
    
    private void clearAllFilters() {
        dateFilterType = "All Time";
        customStartDate = null;
        customEndDate = null;
        filterPresent = true;
        filterAbsent = true;
        updateActiveFiltersDisplay();
        applyAllFilters();
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
            {1, "Chiv Intera", "Software Engineering", "2025-01-31", "Present"},
            {2, "Song Phengroth", "Software Engineering", "2025-01-30", "Present"},
            {3, "John Smith", "Computer Science", "2025-01-30", "Absent"},
            {4, "Emma Wilson", "Software Engineering", "2025-01-29", "Present"},
            {5, "Michael Brown", "Computer Science", "2025-01-29", "Present"},
            {6, "Sarah Davis", "Information Systems", "2025-01-28", "Absent"},
            {7, "David Miller", "Software Engineering", "2025-01-27", "Present"},
            {8, "Lisa Anderson", "Computer Science", "2025-01-24", "Present"},
            {9, "James Taylor", "Software Engineering", "2025-01-20", "Present"},
            {10, "Emily White", "Information Systems", "2025-01-15", "Absent"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
    
    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty() || text.equals("Search student name or ID")) {
            applyAllFilters();
        } else {
            applyAllFilters();
        }
    }
    
    private void showFilterDialog() {
        JDialog filterDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                           "Filter Options", true);
        filterDialog.setLayout(new BorderLayout());
        filterDialog.setSize(480, 600);
        filterDialog.setLocationRelativeTo(this);
        filterDialog.setUndecorated(true);
        
        // Main container - flat colors
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(245, 242, 233));
        mainContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 175), 2));
        
        // Header panel - flat teal color
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(78, 129, 136));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("Filter Options");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Close button
        JLabel closeButton = new JLabel("✕");
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
        closeButton.setForeground(Color.WHITE);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                filterDialog.dispose();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeButton.setForeground(new Color(255, 200, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeButton.setForeground(Color.WHITE);
            }
        });
        headerPanel.add(closeButton, BorderLayout.EAST);
        
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with ScrollPane
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(new Color(245, 242, 233));
        
        // Date Filter Section
        JLabel dateLabel = new JLabel("Filter by Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateLabel.setForeground(new Color(60, 60, 60));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(dateLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        ButtonGroup dateGroup = new ButtonGroup();
        
        // Custom styled radio buttons
        JRadioButton allTimeRadio = createStyledRadioButton("All Time", dateFilterType.equals("All Time"));
        dateGroup.add(allTimeRadio);
        contentPanel.add(allTimeRadio);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JRadioButton yesterdayRadio = createStyledRadioButton("Yesterday", dateFilterType.equals("Yesterday"));
        dateGroup.add(yesterdayRadio);
        contentPanel.add(yesterdayRadio);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JRadioButton thisWeekRadio = createStyledRadioButton("This Week", dateFilterType.equals("This Week"));
        dateGroup.add(thisWeekRadio);
        contentPanel.add(thisWeekRadio);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JRadioButton thisMonthRadio = createStyledRadioButton("This Month", dateFilterType.equals("This Month"));
        dateGroup.add(thisMonthRadio);
        contentPanel.add(thisMonthRadio);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JRadioButton customRadio = createStyledRadioButton("Custom Date Range", dateFilterType.equals("Custom"));
        dateGroup.add(customRadio);
        contentPanel.add(customRadio);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Date picker panel (for custom range)
        JPanel datePickerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        datePickerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        datePickerPanel.setBackground(new Color(218, 209, 193));
        datePickerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 175), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        datePickerPanel.setMaximumSize(new Dimension(420, 50));
        
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fromLabel.setForeground(new Color(80, 80, 80));
        datePickerPanel.add(fromLabel);
        
        JTextField startDateField = new JTextField(10);
        startDateField.setFont(new Font("Arial", Font.PLAIN, 13));
        startDateField.setText(customStartDate != null ? customStartDate.toString() : "yyyy-MM-dd");
        startDateField.setEnabled(customRadio.isSelected());
        startDateField.setBackground(new Color(218, 209, 193));
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 170, 155), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        startDateField.setForeground(new Color(60, 60, 60));
        datePickerPanel.add(startDateField);
        
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.BOLD, 12));
        toLabel.setForeground(new Color(80, 80, 80));
        datePickerPanel.add(toLabel);
        
        JTextField endDateField = new JTextField(10);
        endDateField.setFont(new Font("Arial", Font.PLAIN, 13));
        endDateField.setText(customEndDate != null ? customEndDate.toString() : "yyyy-MM-dd");
        endDateField.setEnabled(customRadio.isSelected());
        endDateField.setBackground(new Color(218, 209, 193));
        endDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 170, 155), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        endDateField.setForeground(new Color(60, 60, 60));
        datePickerPanel.add(endDateField);
        
        customRadio.addActionListener(e -> {
            startDateField.setEnabled(true);
            endDateField.setEnabled(true);
        });
        
        allTimeRadio.addActionListener(e -> {
            startDateField.setEnabled(false);
            endDateField.setEnabled(false);
        });
        yesterdayRadio.addActionListener(e -> {
            startDateField.setEnabled(false);
            endDateField.setEnabled(false);
        });
        thisWeekRadio.addActionListener(e -> {
            startDateField.setEnabled(false);
            endDateField.setEnabled(false);
        });
        thisMonthRadio.addActionListener(e -> {
            startDateField.setEnabled(false);
            endDateField.setEnabled(false);
        });
        
        contentPanel.add(datePickerPanel);
        
        // Separator line
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel separator = new JPanel();
        separator.setMaximumSize(new Dimension(420, 1));
        separator.setBackground(new Color(200, 190, 175));
        contentPanel.add(separator);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Status Filter Section
        JLabel statusLabel = new JLabel("Filter by Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(60, 60, 60));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(statusLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        JCheckBox presentBox = createStyledCheckBox("Present", filterPresent);
        contentPanel.add(presentBox);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JCheckBox absentBox = createStyledCheckBox("Absent", filterAbsent);
        contentPanel.add(absentBox);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setBackground(new Color(245, 242, 233));
        buttonPanel.setMaximumSize(new Dimension(420, 45));
        
        // Apply button - flat design
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setFont(new Font("Arial", Font.BOLD, 14));
        applyButton.setForeground(Color.BLACK);
        applyButton.setBackground(new Color(198, 174, 92));
        applyButton.setFocusPainted(false);
        applyButton.setBorderPainted(false);
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyButton.setPreferredSize(new Dimension(140, 40));
        applyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                applyButton.setBackground(new Color(208, 184, 102));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                applyButton.setBackground(new Color(198, 174, 92));
            }
        });
        applyButton.addActionListener(e -> {
            // Get selected date filter
            if (allTimeRadio.isSelected()) {
                dateFilterType = "All Time";
            } else if (yesterdayRadio.isSelected()) {
                dateFilterType = "Yesterday";
            } else if (thisWeekRadio.isSelected()) {
                dateFilterType = "This Week";
            } else if (thisMonthRadio.isSelected()) {
                dateFilterType = "This Month";
            } else if (customRadio.isSelected()) {
                try {
                    customStartDate = LocalDate.parse(startDateField.getText());
                    customEndDate = LocalDate.parse(endDateField.getText());
                    dateFilterType = "Custom";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(filterDialog, 
                        "Invalid date format. Please use yyyy-MM-dd", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            filterPresent = presentBox.isSelected();
            filterAbsent = absentBox.isSelected();
            
            applyAllFilters();
            updateActiveFiltersDisplay();
            filterDialog.dispose();
        });
        buttonPanel.add(applyButton);
        
        // Cancel button - flat design
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setForeground(new Color(60, 60, 60));
        cancelButton.setBackground(new Color(190, 190, 190));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(110, 40));
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(new Color(210, 210, 210));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(new Color(190, 190, 190));
            }
        });
        cancelButton.addActionListener(e -> filterDialog.dispose());
        buttonPanel.add(cancelButton);
        
        contentPanel.add(buttonPanel);
        
        // Add content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(245, 242, 233));
        
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        
        filterDialog.add(mainContainer);
        filterDialog.setVisible(true);
    }
    
    // Helper method to create styled radio buttons
    private JRadioButton createStyledRadioButton(String text, boolean selected) {
        JRadioButton radio = new JRadioButton(text, selected);
        radio.setFont(new Font("Arial", Font.PLAIN, 14));
        radio.setForeground(new Color(60, 60, 60));
        radio.setAlignmentX(Component.LEFT_ALIGNMENT);
        radio.setBackground(new Color(245, 242, 233));
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radio;
    }
    
    // Helper method to create styled checkboxes
    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
        checkBox.setForeground(new Color(60, 60, 60));
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBox.setBackground(new Color(245, 242, 233));
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return checkBox;
    }
    
    private void applyAllFilters() {
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        
        // Search filter
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty() && !searchText.equals("Search student name or ID")) {
            filters.add(RowFilter.regexFilter("(?i)" + searchText));
        }
        
        // Date filter
        if (!dateFilterType.equals("All Time")) {
            LocalDate today = LocalDate.now();
            LocalDate startDate = null;
            LocalDate endDate = today;
            
            switch (dateFilterType) {
                case "Yesterday":
                    startDate = today.minusDays(1);
                    endDate = today.minusDays(1);
                    break;
                case "This Week":
                    startDate = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                    break;
                case "This Month":
                    startDate = today.with(TemporalAdjusters.firstDayOfMonth());
                    break;
                case "Custom":
                    startDate = customStartDate;
                    endDate = customEndDate;
                    break;
            }
            
            if (startDate != null) {
                final LocalDate finalStartDate = startDate;
                final LocalDate finalEndDate = endDate;
                
                filters.add(new RowFilter<DefaultTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                        String dateStr = entry.getStringValue(3); // Date column
                        try {
                            LocalDate rowDate = LocalDate.parse(dateStr);
                            return !rowDate.isBefore(finalStartDate) && !rowDate.isAfter(finalEndDate);
                        } catch (Exception e) {
                            return false;
                        }
                    }
                });
            }
        }
        
        // Status filter
        if (!filterPresent || !filterAbsent) {
            if (filterPresent) {
                filters.add(RowFilter.regexFilter("Present", 4));
            } else if (filterAbsent) {
                filters.add(RowFilter.regexFilter("Absent", 4));
            } else {
                filters.add(RowFilter.regexFilter("^$", 4)); // Show nothing
            }
        }
        
        // Apply combined filter
        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }
    
    public void setClassInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseCode = courseCode;
        titleLabel.setText("> Class > Attendance > Attendance List");
    }
}