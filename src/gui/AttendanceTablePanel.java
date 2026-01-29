package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AttendanceTablePanel extends JPanel {

    public AttendanceTablePanel(String className) {
        setBackground(new Color(58, 68, 70)); // Dark slate background
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // 1. Breadcrumb Header: > Class > Attendance
        JLabel breadcrumb = new JLabel("> " + className + " > Attendance");
        breadcrumb.setForeground(Color.WHITE);
        breadcrumb.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(breadcrumb, BorderLayout.NORTH);

        // 2. The Student List Table Container
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(new Color(214, 210, 196)); // Beige background
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table Title inside the beige area
        JLabel tableTitle = new JLabel("Student list");
        tableTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        tableContainer.add(tableTitle, BorderLayout.NORTH);

        // Define Columns: No., Name, Major, Date, Status
        String[] columns = {"No.", "Name", "Major", "Date", "Status"};

        // Sample Data - In a real app, you'd pull this from your Student models
        Object[][] data = {
                {"1", "Sovan Kiry", "Computer Science", "2024-05-20", "Present"},
                {"2", "Chanlina Keo", "Computer Science", "2024-05-20", "Absent"},
                {"3", "Rithy Sak", "Information Tech", "2024-05-20", "Present"},
                {"4", "Bopha Nuon", "Software Eng", "2024-05-20", "Late"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);

        // Table Styling to match your image
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);
        table.setBackground(new Color(214, 210, 196));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(214, 210, 196));
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(214, 210, 196));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Wrap container to give it rounded look via a outer panel or margin
        add(tableContainer, BorderLayout.CENTER);
    }
}
