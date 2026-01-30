
import gui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Start the GUI Login Screen
        // We use invokeLater to ensure the UI runs safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}