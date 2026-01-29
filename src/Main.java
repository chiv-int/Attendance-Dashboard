
import gui.LoginFrame;
import models.AttendanceDashboard;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize the Backend (The "Face")
        // This object carries the data logic through the whole app
        AttendanceDashboard dashboard = new AttendanceDashboard();

        // 2. Start the GUI (The "Mask")
        // We use invokeLater to ensure the UI runs safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(dashboard);
            login.setVisible(true);
        });
    }
}