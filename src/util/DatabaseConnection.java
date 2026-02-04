package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility for MySQL
 * Manages database connections using singleton pattern
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_dashboard";
    private static final String DB_USER = "root"; // Change to your MySQL username
    private static final String DB_PASSWORD = "chiv279520"; // Change to your MySQL password
    
    private static DatabaseConnection instance;
    private Connection connection;

    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL driver loaded");
            // Establish connection
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Enable auto-commit for immediate updates
            this.connection.setAutoCommit(true);
            System.out.println("✓ Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            System.err.println("Please add mysql-connector-java to your classpath");
            e.printStackTrace();
            this.connection = null;
        } catch (SQLException e) {
            System.err.println("✗ Failed to connect to database!");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            System.err.println("Please ensure MySQL is running and credentials are correct");
            e.printStackTrace();
            this.connection = null;
        }
    }

    /**
     * Get singleton instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Get database connection
     */
    public Connection getConnection() {
        try {
            // Check if connection is closed or null, recreate if needed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting database connection!");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("✗ Error closing database connection!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
