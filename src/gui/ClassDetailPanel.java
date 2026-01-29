package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ClassDetailPanel extends JPanel {
    private String className;
    private TeacherGUI parentFrame; // Added this field

    // FIX: Constructor now accepts 2 arguments (String and TeacherGUI)
    public ClassDetailPanel(String className, TeacherGUI parentFrame) {
        this.className = className;
        this.parentFrame = parentFrame; // Save the reference to the main window

        setBackground(new Color(58, 68, 70));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Heading
        JLabel head = new JLabel("> " + className);
        head.setForeground(Color.WHITE);
        head.setFont(new Font("SansSerif", Font.BOLD, 24));
        head.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(head);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // Items
        add(createDetailItem("+ Attendance"));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createDetailItem("+ Week 1"));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createDetailItem("+ Week 2"));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createDetailItem("+ Week 3"));
    }

    private JPanel createDetailItem(String text) {
        JPanel item = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(214, 210, 196));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
            }
        };
        item.setMaximumSize(new Dimension(800, 60));
        item.setPreferredSize(new Dimension(800, 60));
        item.setOpaque(false);
        item.setLayout(new BorderLayout());
        item.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setForeground(new Color(156, 126, 101));
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        item.add(label, BorderLayout.CENTER);

        // FIX: Add the MouseListener to detect the "+ Attendance" click
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (text.equals("+ Attendance")) {
                    // Tell the TeacherGUI to switch to the table view
                    parentFrame.showAttendanceTable(className);
                }
            }
        });

        return item;
    }
}