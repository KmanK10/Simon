import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;

public class Scene implements ChangeListener, KeyListener {
    // Variables:
    // Field variables
    private int height; // The width of the window
    private int width; // The height of the window
    private String windowName; // The name of the window
    private Color bgColor = Color.WHITE; // The background color of the game
    private File file;
    private FileWriter writer;
    private int hiScore = 0;
    private int score;
    // Program variables
    private static Font btnFont = new Font(Font.SANS_SERIF, Font.PLAIN, 25); // The font for buttons
    private static Font H1 = new Font(Font.SANS_SERIF, Font.BOLD, 50); // The font for the H1s
    private static Font H2 = new Font(Font.SANS_SERIF, Font.BOLD, 25); // The font for the H2s
    private static Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15); // The font for labels
    // Components
    private JFrame colorFrame = new JFrame();
    private JColorChooser picker = new JColorChooser();
    private JFrame fileFrame = new JFrame();
    private JFileChooser chooser = new JFileChooser();
    private JFrame frame = new JFrame();
    private JPanel container = new JPanel();
    private JPanel mainMenu = new JPanel();
    private JLabel title = new JLabel();
    private JButton startBtn = new JButton();
    private JLabel optionLabel = new JLabel();
    private JButton bgBtn = new JButton();
    private JPanel fileContainer = new JPanel();
    private JButton resetDataBtn;
    private JButton resetPathBtn;
    private JButton fileBtn = new JButton();
    private JLabel about = new JLabel();
    private JButton greenBtn = new JButton();
    private JButton redBtn = new JButton();
    private JButton yellowBtn = new JButton();
    private JButton blueBtn = new JButton();

    /**
     * The default constructor.
     *
     * @param width      The width of the window
     * @param height     The height of the window
     * @param windowName The name of the window
     * @author Kiefer Menard
     */
    public Scene(int width, int height, String windowName) {
        // Variables
        this.height = height;
        this.width = width;
        this.windowName = windowName;

        initialize();
    }

    /**
     * The initialize method sets up the window with the proper UI elements.
     *
     * @author Kiefer Menard
     */
    private void initialize() {
        try {
            frame.setTitle(windowName);
            frame.setLayout(new BorderLayout(10, 5));
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setSize(width, height);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setBackground(bgColor);

            colorFrame.setTitle("Color Picker");
            colorFrame.setLayout(new BorderLayout());
            colorFrame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
            colorFrame.setSize(700, 450);
            colorFrame.setResizable(false);
            colorFrame.setLocationRelativeTo(null);
            colorFrame.setBackground(bgColor);
            colorFrame.add(picker);

            fileFrame.setTitle("Choose File");
            fileFrame.setLayout(new BorderLayout());
            fileFrame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
            fileFrame.setSize(700, 450);
            fileFrame.setResizable(false);
            fileFrame.setLocationRelativeTo(null);
            fileFrame.setBackground(bgColor);
            fileFrame.add(chooser);

            picker.getSelectionModel().addChangeListener(this);

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));

            container.setLayout(new BorderLayout(10, 0));
            container.setFocusable(true);
            System.out.println(container.requestFocusInWindow());

            mainMenu.setLayout(new GridLayout(6, 1, 0, 2));

            createLabel(title, "Simon", mainMenu, H1);

            createBtn(startBtn, "Play", mainMenu);

            createLabel(optionLabel, "Options", mainMenu, H2);

            createBtn(bgBtn, "Change background color", mainMenu);
            bgBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

            fileContainer.setLayout(new BorderLayout());
            fileContainer.setBackground(bgColor);

            resetDataBtn  = new JButton("<HTML>Reset save data<br>to default</HTML>");
            fileContainer.add(resetDataBtn, BorderLayout.WEST);
            resetDataBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            resetDataBtn.setOpaque(true);

            resetPathBtn  = new JButton("<HTML>Reset file path<br>to default</HTML>");
            fileContainer.add(resetPathBtn, BorderLayout.EAST);
            resetPathBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            resetPathBtn.setOpaque(true);

            mainMenu.add(fileContainer);

            createBtn(fileBtn, "Change save file", mainMenu);
            fileBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

            container.add(mainMenu, BorderLayout.WEST);

            createLabel(about, "", container, labelFont);

            frame.add(container);
            frame.setVisible(true);

            greenBtn.setBackground(Color.GREEN.darker());
            greenBtn.setOpaque(true);
            redBtn.setBackground(Color.RED.darker());
            redBtn.setOpaque(true);
            yellowBtn.setBackground(Color.YELLOW.darker());
            yellowBtn.setOpaque(true);
            blueBtn.setBackground(Color.BLUE.darker());
            blueBtn.setOpaque(true);

            if (file == null) {
                file = new File("./simon.txt");
                fileSetup();
            }

            chooser.addActionListener(e -> {
                File tempFile = chooser.getSelectedFile();

                if (tempFile != null && (tempFile.getName().indexOf('.') == -1 || tempFile.getName().substring(tempFile.getName().indexOf('.')).equals(".txt"))) {
                    if (e.getActionCommand().equals("ApproveSelection")) {
                        file = tempFile;
                    }

                    fileFrame.dispose();

                    fileSetup();
                }
            });

            startBtn.addActionListener(e -> playGame());

            bgBtn.addActionListener(e -> {
                picker.setColor(bgColor);
                colorFrame.setVisible(true);
            });

            resetDataBtn.addActionListener(e -> {
                bgColor = Color.WHITE;
                refreshColors();
                hiScore = 0;
                writeFile();
            });

            resetPathBtn.addActionListener(e -> {
                file = new File("./simon.txt");
                fileSetup();
            });

            fileBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "<HTML>Please select a text file or directory for storing save data.<br>If a file isn't chosen, a default text file called <em>simon.txt</em><br>will be generated in the selected directory.</HTML>", "Choose File", JOptionPane.PLAIN_MESSAGE);
                fileFrame.setVisible(true);
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * The playGame method runs the program flow once the game is running.
     *
     * @author Kiefer Menard
     */
    private void playGame() {
        // Variables
        score = 0; // Reset score

        container.removeAll();
        container.revalidate();
        container.repaint();

        container.setLayout(new GridLayout(2,2));
        container.add(greenBtn);
        container.add(yellowBtn);
        container.add(redBtn);
        container.add(blueBtn);
    }

    /**
     * The endGame method handles shutting down the game and going back to the main menu.
     *
     * @author Kiefer Menard
     */
    private void endGame() {
        container.removeAll();
        container.revalidate();
        container.repaint();

        hiScore = max(score, hiScore);

        initialize();
        fileSetup();
    }

    /**
     * The fileSetup method handles everything to do with the save file.
     * Should be run whenever a new file is selected by the user.
     *
     * @author Kiefer Menard
     */
    private void fileSetup() {
        // Variables:
        String line = "";

        try {
            if (file.isDirectory()) {
                file = new File(file.getPath() + "/simon.txt");
            }

            // Check to make sure our file exists
            if (file.createNewFile()) {
                writeFile();
            }

            Scanner scanner = new Scanner(file);

            try {
                for (int i = 1; i <= 2; i++) {
                    line = scanner.nextLine();

                    switch (i) {
                        case 1:
                            bgColor = new Color(parseInt(line));
                            refreshColors();
                            break;
                        case 2: hiScore = parseInt(line);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Incorrect file data.");
            }

            about.setText("<HTML>Welcome to Simon.<br><br>Your hi-score is " + hiScore + ".<br><br>How to play:<br>There are four buttons – green, red, yellow and blue. When one button lights up, click on the same one. The sequence begins with a single flash, followed by a repeat of the previous button and a quick flash of a different button. Your task is to replicate this pattern. The complexity increases with each successful round, adding an extra flash each time. The game continues until an incorrect pattern repetition occurs.<br><br>Based on the game by <u>Ralph H. Baer and Howard J. Morrison</u>.<br>Developed by <u>Kiefer Menard</u>.<br><br>Save file path: <strong>" + (file.getPath().equals("./simon.txt") ? "Default" : "<em>" + file.getPath() + "</em>") + "</strong></HTML>");

            scanner.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * The writeFile method writes the correct info to the save file so that it can be saved for future use.
     * Saves the users highest score and the background color, meaning it should be run whenever those change.
     *
     * @author Kiefer Menard
     */
    private void writeFile() {
        try {
            writer = new FileWriter(file);

            writer.write(bgColor.getRGB() + "\n" + hiScore);

            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * The refreshColors method handles setting all the colors of the UI elements based off the background color variable.
     * Should be run everytime the user changes the background color or changes the save file.
     *
     * @author Kiefer Menard
     */
    private void refreshColors() {
        Color textColor;
        Color bgAdjusted;

        if ((double) (bgColor.getRed() + bgColor.getGreen() + bgColor.getBlue()) / 3 > 127.5) {
            textColor = Color.BLACK;
            bgAdjusted = bgColor.darker();
        } else {
            textColor = Color.WHITE;
            bgAdjusted = bgColor.brighter();
        }

        frame.setBackground(bgColor);
        container.setBackground(bgColor);
        mainMenu.setBackground(bgColor);
        title.setForeground(textColor);
        startBtn.setBackground(bgAdjusted);
        optionLabel.setForeground(textColor);
        bgBtn.setBackground(bgAdjusted);
        fileContainer.setBackground(bgColor);
        resetDataBtn.setBackground(bgAdjusted);
        resetPathBtn.setBackground(bgAdjusted);
        fileBtn.setBackground(bgAdjusted);
        about.setBackground(bgColor);
        about.setForeground(textColor);
    }

    /**
     * The keyTyped method handles keyboard inputs.
     *
     * @author Kiefer Menard
     */
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    /**
     * The keyPressed method handles key presses on the keyboard.
     *
     * @author Kiefer Menard
     */
    @Override
    public void keyPressed(KeyEvent e) {}
    /**
     * The keyReleased method handles key releases on the keyboard.
     *
     * @author Kiefer Menard
     */
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * The stateChanged method handles a change in the state of the color picker. I.e. when the user selects a new color.
     *
     * @author Kiefer Menard
     */
    public void stateChanged(ChangeEvent e) {
        if (picker.getColor().getAlpha() == 255) {
            bgColor = picker.getColor();
            writeFile();
            refreshColors();
        }
    }

    /**
     * The createBtn method initializes a JButton with all the necessary attributes.
     *
     * @param button Which JButton object to initialize.
     * @param text What text to display on the button.
     * @param panel Which JPanel to add the button to.
     * @return Returns the JButton object.
     * @author Kiefer Menard
     */
    private JButton createBtn(JButton button, String text, JPanel panel) {
        button.setText(text);
        button.setFont(btnFont);
        button.setOpaque(true);

        panel.add(button);

        return button;
    }

    /**
     * The createLabel method initializes a JLabel with all the necessary attributes.
     *
     * @param label Which JLabel object to initialize.
     * @param text What text to display in the label.
     * @param panel Which JPanel to add the label to.
     * @param font Which font to use.
     * @return Returns the JLabel object.
     * @author Kiefer Menard
     */
    private JLabel createLabel(JLabel label, String text, JPanel panel, Font font) {
        label.setText(text);
        label.setFont(font);

        panel.add(label);

        return label;
    }
}