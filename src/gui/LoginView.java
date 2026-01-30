package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * LoginView - Login screen for both teachers and students
 */
public class LoginView extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private BufferedImage logoImage;
    
    public LoginView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(75, 85, 90)); // Dark gray background from Figma
        
        initComponents();
    }
    
    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Create main login panel with beige background and rounded corners
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
            }
        };
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(218, 209, 193)); // Beige color from Figma
        loginPanel.setOpaque(false); // Important for rounded corners
        loginPanel.setBorder(new EmptyBorder(40, 60, 50, 60));
        
        // Logo circle
        CircularLogoPanel logoPanel = new CircularLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(logoPanel);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // School Name
        JLabel schoolLabel = new JLabel("School Name");
        schoolLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        schoolLabel.setForeground(Color.BLACK);
        schoolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(schoolLabel);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // ID field
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idLabel.setForeground(Color.BLACK);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(idLabel);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextField idField = new JTextField(20);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idField.setMaximumSize(new Dimension(400, 35));
        idField.setBackground(Color.WHITE);
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(idField);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(usernameLabel);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(400, 35));
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(usernameField);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(passwordLabel);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(400, 35));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(passwordField);
        
        loginPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        
        // Login button (brown color, right-aligned)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(400, 45));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(139, 115, 85)); // Brown color from Figma
        loginButton.setPreferredSize(new Dimension(110, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        
        buttonPanel.add(loginButton);
        loginPanel.add(buttonPanel);
        
        add(loginPanel, gbc);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TODO: Connect to backend authentication
        // For now, simple demo logic
        if (username.equals("teacher1") && password.equals("pass123")) {
            mainFrame.handleLoginSuccess("TEACHER");
        } else if (username.equals("student1") && password.equals("pass123")) {
            mainFrame.handleLoginSuccess("STUDENT");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Inner class for circular logo panel
     */
    class CircularLogoPanel extends JPanel {
        private static final int CIRCLE_SIZE = 120;
        
        public CircularLogoPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(CIRCLE_SIZE, CIRCLE_SIZE));
            loadLogo();
        }
        
        private void loadLogo() {
            try {
                File logoFile = new File("Logo.jpg");
                if (logoFile.exists()) {
                    logoImage = ImageIO.read(logoFile);
                }
            } catch (Exception e) {
                System.out.println("Logo.jpg not found. Using placeholder.");
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw circular background - dark gray like Figma
            g2d.setColor(new Color(60, 65, 68));
            g2d.fillOval(0, 0, CIRCLE_SIZE, CIRCLE_SIZE);
            
            // Draw logo if available, otherwise leave it solid
            if (logoImage != null) {
                // Create circular clip
                Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, CIRCLE_SIZE, CIRCLE_SIZE);
                g2d.setClip(circle);
                
                // Draw scaled image
                g2d.drawImage(logoImage, 0, 0, CIRCLE_SIZE, CIRCLE_SIZE, null);
            }
            
            g2d.dispose();
        }
    }
}