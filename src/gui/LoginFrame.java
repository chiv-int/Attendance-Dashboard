package gui;

import models.AttendanceDashboard;
import models.Teacher;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import models.Teacher;

public class LoginFrame extends JFrame {
    private final JTextField userField;
    private final JPasswordField passField;

    public LoginFrame() {
        setTitle("Log In");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(43, 52, 54)); // Dark slate background
        setLayout(new GridBagLayout());

        // The Rounded Login Card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(214, 210, 196)); // Beige card color
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        card.setPreferredSize(new Dimension(450, 550));
        card.setOpaque(false);
        card.setLayout(null);

        // School Logo Placeholder
        JPanel logoCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(43, 52, 54));
                g.fillOval(0, 0, 100, 100);
            }
        };
        logoCircle.setBounds(175, 40, 100, 100);
        logoCircle.setOpaque(false);
        card.add(logoCircle);

        JLabel lblSchool = new JLabel("School Name", SwingConstants.CENTER);
        lblSchool.setBounds(0, 150, 450, 30);
        lblSchool.setFont(new Font("SansSerif", Font.PLAIN, 18));
        card.add(lblSchool);

        // Input Fields
        createLabel(card, "Username", 200);
        userField = createInputField(card, 225);
        createLabel(card, "Password", 285);
        passField = new JPasswordField();
        passField.setBounds(50, 310, 350, 40);
        passField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.add(passField);

        // Log In Button
        JButton btnLogin = new JButton("Log In");
        btnLogin.setBounds(300, 385, 100, 40);
        btnLogin.setBackground(new Color(156, 126, 101)); // Brown button color
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(e -> handleLogin());
        card.add(btnLogin);

        add(card);
    }

    private void createLabel(JPanel p, String text, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(50, y, 100, 20);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        p.add(l);
    }

    private JTextField createInputField(JPanel p, int y) {
        JTextField f = new JTextField();
        f.setBounds(50, y, 350, 40);
        f.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        p.add(f);
        return f;
    }

    private void handleLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        // We call our local helper to find the teacher
        Teacher authenticatedTeacher = findTeacherLocally(username, password);

        if (authenticatedTeacher != null) {
            this.dispose();
            new TeacherGUI(authenticatedTeacher).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.");
        }
    }

    // This method acts as your GUI's "Local Database"
    // since the real one in models is private.
    private Teacher findTeacherLocally(String user, String pass) {
        if (user.equals("teacher1") && pass.equals("pass123")) {
            return new Teacher("U003", "teacher1", "pass123", "Mr. Tal Tongsreng", "T001", "CS", "Data Structures");
        }
        if (user.equals("teacher2") && pass.equals("pass456")) {
            return new Teacher("U004", "teacher2", "pass456", "Ms. Srey Leak", "T002", "IT", "Java Programming");
        }
        // Add more teachers here as needed
        return null;
    }
}