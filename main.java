import javax.swing.SwingUtilities;
import gui.EcoFrame;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Application entry point. Launches the GUI on the Event Dispatch Thread.
 */
public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcoFrame::new);
    }
}
