package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TeacherDashboard extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private MainFrame mainFrame;

    // Colors from the provided design
    private final Color COLOR_BG = new Color(64, 72, 74);       // Dark Slate
    private final Color COLOR_NAV = new Color(50, 50, 50);      // Darker Navigation
    private final Color COLOR_CARD = new Color(218, 209, 193);  // Beige Card
    private final Color COLOR_CLASS_TEXT = new Color(139, 115, 85); // Brown Text

    public TeacherDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // 1. Navigation Bar
        add(createNavBar(), BorderLayout.NORTH);

        // 2. Main Content Area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        // View 1: Home (Empty/Nothing)
        JPanel homeView = new JPanel();
        homeView.setBackground(COLOR_BG);
        
        // View 2: Classes (The List)
        JPanel classesView = createClassesList();

        contentPanel.add(homeView, "HOME");
        contentPanel.add(classesView, "CLASSES");

        add(contentPanel, BorderLayout.CENTER);
        
        // Start on Home (Nothing)
        cardLayout.show(contentPanel, "HOME");
    }

    private JPanel createNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_NAV);
        nav.setPreferredSize(new Dimension(0, 60));
        nav.setBorder(new EmptyBorder(0, 30, 0, 30));

        // Logo
        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        nav.add(logoLabel, BorderLayout.WEST);

        // Center Menu
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        menuPanel.setOpaque(false);

        JButton homeBtn = createNavButton("Home");
        JButton classesBtn = createNavButton("Classes");

        // Action Listeners for switching views
        homeBtn.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));
        classesBtn.addActionListener(e -> cardLayout.show(contentPanel, "CLASSES"));

        menuPanel.add(homeBtn);
        menuPanel.add(classesBtn);
        nav.add(menuPanel, BorderLayout.CENTER);

        // Profile Placeholder
        JLabel pfpLabel = new JLabel("pfp");
        pfpLabel.setForeground(Color.WHITE);
        nav.add(pfpLabel, BorderLayout.EAST);

        return nav;
    }

    private JPanel createClassesList() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(COLOR_BG);
        listPanel.setBorder(new EmptyBorder(30, 60, 30, 60));

        // Dummy Data for classes
        listPanel.add(createClassCard("Computer Science 101", "Central University"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        listPanel.add(createClassCard("Software Engineering", "Tech Institute"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        listPanel.add(createClassCard("UI/UX Design", "Central University"));

        // Make it scrollable if the list is long
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(scrollPane);
        return container;
    }

    private JPanel createClassCard(String className, String uniName) {
        // Custom panel with rounded corners
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(800, 120));
        card.setMaximumSize(new Dimension(1200, 120));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Text Content
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(className);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameLabel.setForeground(COLOR_CLASS_TEXT);

        JLabel universityLabel = new JLabel(uniName);
        universityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        universityLabel.setForeground(Color.BLACK);

        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(universityLabel);

        card.add(textPanel, BorderLayout.WEST);
        return card;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(new Color(180, 180, 180)); // Dim by default
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect to highlight
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setForeground(Color.WHITE); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setForeground(new Color(180, 180, 180)); }
        });
        
        return btn;
    }
}