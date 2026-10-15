package ayudadesk;

import ayudadesk.model.AppData;
import ayudadesk.ui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// PROGRAM START
public class Main {

    public static void main(String[] args) {
        // Use the look of the operating system (Windows, Mac, ...)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If it fails, Swing's default look is used
        }

        // Swing windows must be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            AppData data = new AppData();
            new LoginFrame(data).setVisible(true);
        });
    }
}
