import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Scene {
    // Variables:
    // Field variables
    private int height; // The width of the window
    private int width; // The height of the window
    private String windowName; // The name of the window
    // User settings
    private Color bgColor = Color.WHITE; // The background color of the game
    // Program variables
    private static Font btnFont  = new Font(Font.SANS_SERIF, Font.PLAIN,  25); // The font for buttons
    private static Font H1 = new Font(Font.SANS_SERIF, Font.BOLD,  50); // The font for the H1s
    private static Font H2 = new Font(Font.SANS_SERIF, Font.BOLD,  25); // The font for the H2s
    private static Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN,  15); // The font for labels

    /**
     * The default constructor.
     * @author Kiefer Menard
     * @param width The width of the window
     * @param height The height of the window
     * @param windowName The name of the window
     */
    public Scene(int width, int height, String windowName) {
        // Variables
        this.height = height;
        this.width = width;
        this.windowName = windowName;

        initialize();
    }

    private void initialize() {
        JFrame frame = new JFrame();

        frame.setTitle(windowName);
        frame.setLayout(new BorderLayout(10, 5));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel menuContainer = new JPanel();
        menuContainer.setLayout(new BorderLayout(10, 0));
        menuContainer.setBackground(bgColor);

        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new GridLayout(5, 1));
        mainMenu.setBackground(bgColor);

        JLabel title = createLabel("Simon", mainMenu, H1);

        JButton startBtn = createBtn("Play", mainMenu);

        JLabel optionLabel = createLabel("Options", mainMenu, H2);

        JButton bgBtn = createBtn("Change background color", mainMenu);
        bgBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,  15));

        menuContainer.add(mainMenu, BorderLayout.WEST);

        JLabel about = createLabel("<HTML>Welcome to Simon.<br><br>Your hi-score is 0.<br><br>How to play:<br>There are four buttons — red, green, blue, and yellow. When one button lights up, click on the same one. The sequence begins with a single flash, followed by a repeat of the previous button and a quick flash of a different button. Your task is to replicate this pattern. The complexity increases with each successful round, adding an extra flash each time. The game continues until an incorrect pattern repetition occurs.<br><br>Developed by Kiefer Menard.</HTML>\n", menuContainer, labelFont);
        about.setFont(labelFont);

        frame.add(menuContainer);
        frame.setVisible(true);

        startBtn.addActionListener(e -> Main.startGame());
        //bgBtn.addActionListener();
    }

    public JButton createBtn(String text, JPanel panel) {
        JButton button = new JButton(text);
        button.setFont(btnFont);
        button.setMargin(new Insets(2, 5, 2, 5));

        panel.add(button);

        return button;
    }

    public JLabel createLabel(String text, JPanel panel, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);

        panel.add(label);

        return label;
    }
}