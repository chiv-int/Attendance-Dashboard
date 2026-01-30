package gui;

import javax.swing.*;
import java.awt.*;

public class SchoolLoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SchoolLoginUI() {
        setTitle("School Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Teal background
                g2d.setColor(new Color(78, 129, 136));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        
        // Cream-colored card panel
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Cream background with rounded corners
                g2d.setColor(new Color(245, 242, 233));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                super.paintComponent(g);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setOpaque(false);
        cardPanel.setBounds(300, 100, 300, 390);
        
        // Profile avatar from filepath
        JLabel avatarLabel = new JLabel();
        ImageIcon avatar = new ImageIcon("C:\\Users\\M\\Kimter-GitPull\\Attendance-Dashboard\\src\\gui\\Logo.jpg");
        Image scaledImage = avatar.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        avatarLabel.setIcon(new ImageIcon(scaledImage));
        avatarLabel.setBounds(125, 15, 50, 50);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(avatarLabel);
        
        // School Name label
        JLabel nameLabel = new JLabel("The Valley University");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(60, 60, 60));
        nameLabel.setBounds(20, 70, 260, 30);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(nameLabel);
        
        // ID label
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        idLabel.setForeground(new Color(100, 100, 100));
        idLabel.setBounds(20, 110, 260, 20);
        cardPanel.add(idLabel);
        
        // ID text field
        JTextField idField = new JTextField();
        idField.setBounds(20, 130, 260, 35);
        idField.setFont(new Font("Arial", Font.PLAIN, 12));
        idField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        cardPanel.add(idField);
        
        // Username label
        // Username text field - assign to class field
        usernameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameLabel.setForeground(new Color(100, 100, 100));
        usernameLabel.setBounds(20, 175, 260, 20);
        cardPanel.add(usernameLabel);
        
        // Username text field
        // Username text field - assign to class field
        usernameField = new JTextField();
        usernameField.setBounds(20, 195, 260, 35);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        cardPanel.add(usernameField);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordLabel.setForeground(new Color(100, 100, 100));
        passwordLabel.setBounds(20, 240, 260, 20);
        cardPanel.add(passwordLabel);
        
        // Password field
        // Password field - assign to class field
        passwordField = new JPasswordField();
        passwordField.setBounds(20, 260, 260, 35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        cardPanel.add(passwordField);
        
       // Log In button - using JLabel
        JLabel loginButton = new JLabel("Log In") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(168, 126, 79));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        loginButton.setBounds(175, 310, 105, 40);
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setForeground(Color.WHITE);
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setVerticalAlignment(SwingConstants.CENTER);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleLogin();
            }
        });
cardPanel.add(loginButton);
                
        mainPanel.add(cardPanel);
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim().toLowerCase();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (username.equals("student")) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                SchoolHomeUI homeFrame = new SchoolHomeUI();
                homeFrame.setVisible(true);
            });
        } else if (username.equals("teacher")) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username. Use 'student' or 'teacher'.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}