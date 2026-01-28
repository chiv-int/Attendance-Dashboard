package gui;

import models.Teacher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class TeacherGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContent;
    private JPanel classesPage;

    public TeacherGUI(Teacher teacher) {
        setTitle("Teacher Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Top Navigation Bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(43, 52, 54));
        navBar.setPreferredSize(new Dimension(0, 60));
        navBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logo = new JLabel("Logo");
        logo.setForeground(Color.GRAY);
        navBar.add(logo, BorderLayout.WEST);

        JPanel navLinks = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        navLinks.setOpaque(false);

        JButton btnHome = createNavLink("Home");
        JButton btnClasses = createNavLink("Classes");
        navLinks.add(btnHome);
        navLinks.add(btnClasses);
        navBar.add(navLinks, BorderLayout.CENTER);

        JLabel pfp = new JLabel("pfp");
        pfp.setForeground(Color.GRAY);
        navBar.add(pfp, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        // 2. Main Content Area (CardLayout)
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(new Color(58, 68, 70));

        classesPage = new JPanel(new BorderLayout());
        classesPage.setBackground(new Color(58, 68, 70));

        mainContent.add(createHomePanel(), "HOME");
        mainContent.add(classesPage, "CLASSES");

        add(mainContent, BorderLayout.CENTER);

        btnHome.addActionListener(e -> cardLayout.show(mainContent, "HOME"));
        btnClasses.addActionListener(e -> {
            classesPage.removeAll();
            classesPage.revalidate();
            classesPage.repaint();
            cardLayout.show(mainContent, "CLASSES");
        });
    }

    private JButton createNavLink(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JComponent createHomePanel() {
        JPanel listPanel = new JPanel();
        listPanel.setBackground(new Color(58, 68, 70));
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        listPanel.add(createClassCard("CS301", "Data Structures & Algorithms"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        listPanel.add(createClassCard("CS402", "Machine Learning"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        listPanel.add(createClassCard("CS205", "Database Systems"));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createClassCard(String code, String name) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(214, 210, 196));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        card.setMaximumSize(new Dimension(1000, 100));
        card.setPreferredSize(new Dimension(1000, 100));
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel title = new JLabel("<html><font color='#9C7E65' size='5'><b>" + code + "</b></font><br><font color='gray'>" + name + "</font></html>");
        card.add(title, BorderLayout.WEST);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showClassDetail(code);
            }
        });

        return card;
    }

    // UPDATED: Now passes 'this' (the TeacherGUI) to the detail panel
    private void showClassDetail(String code) {
        classesPage.removeAll();
        ClassDetailPanel detailView = new ClassDetailPanel(code, this);
        classesPage.add(detailView, BorderLayout.CENTER);
        classesPage.revalidate();
        classesPage.repaint();
        cardLayout.show(mainContent, "CLASSES");
    }

    // NEW: Method specifically to show the Attendance Table
    public void showAttendanceTable(String code) {
        classesPage.removeAll();
        AttendanceTablePanel tablePanel = new AttendanceTablePanel(code);
        classesPage.add(tablePanel, BorderLayout.CENTER);
        classesPage.revalidate();
        classesPage.repaint();
        cardLayout.show(mainContent, "CLASSES");
    }
}