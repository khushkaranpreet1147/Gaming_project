import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch the graphical user interface window safely
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}