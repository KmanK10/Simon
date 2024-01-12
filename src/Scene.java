import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.keyboard.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;

public class Scene implements ChangeListener, NativeKeyListener {
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
    private AtomicInteger input = new AtomicInteger();
    ArrayList<Integer> pattern;
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
    private JPanel btnContainer = new JPanel();
    private JButton greenBtn = new JButton();
    private JButton redBtn = new JButton();
    private JButton yellowBtn = new JButton();
    private JButton blueBtn = new JButton();
    // Flags
    private AtomicBoolean btnPressed = new AtomicBoolean(false);
    // Thread
    private Runnable runnable = new Runnable(){
        public void run(){
            long lastMillis;

            while(true) {
                // Pause for 0.75 sec
                lastMillis = System.currentTimeMillis();
                while(System.currentTimeMillis() < lastMillis + 750);

                pattern.add((int)(Math.random() * 4) + 1);

                for (Integer i : pattern) {
                    System.out.println("Button: " + i);

                    switch (i) {
                        case 1: greenBtn.setBackground(Color.GREEN.brighter()); break;
                        case 2: redBtn.setBackground(Color.PINK); break;
                        case 3: yellowBtn.setBackground(Color.YELLOW.brighter()); break;
                        case 4: blueBtn.setBackground(Color.CYAN); break;
                    }

                    // Pause for 0.25 sec
                    lastMillis = System.currentTimeMillis();
                    while(System.currentTimeMillis() < lastMillis + 250);

                    switch (i) {
                        case 1: greenBtn.setBackground(Color.GREEN.darker()); break;
                        case 2: redBtn.setBackground(Color.RED.darker()); break;
                        case 3: yellowBtn.setBackground(Color.YELLOW.darker()); break;
                        case 4: blueBtn.setBackground(Color.BLUE.darker()); break;
                    }

                    // Pause for 0.25 sec
                    lastMillis = System.currentTimeMillis();
                    while(System.currentTimeMillis() < lastMillis + 250);

                    btnPressed.set(false);
                }

                for (int i : pattern) {
                    System.out.println("Waiting for button press");
                    while (!btnPressed.get()) {
                        lastMillis = System.currentTimeMillis();
                        while (System.currentTimeMillis() < lastMillis + 1) ;
                    } // Wait for button press

                    btnPressed.set(false);

                    System.out.println("Press registered: " + input + " Should have been: " + i);

                    if (input.get() != i) {
                        endGame();
                        return;
                    }
                }

                score++;
                System.out.println("Next iteration");
            }
        }
    };
    Executor executor = Executors.newSingleThreadExecutor();

    /**
     * The default constructor.
     *
     * @author Kiefer Menard
     */
    public Scene() {
        // Variables
        height = 500;
        width = 800;
        windowName = "Simon";

        initialize();
    }

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
            resetDataBtn.setToolTipText("Reset the saved background color to white and the hi-score to 0.");

            resetPathBtn  = new JButton("<HTML>Reset file path<br>to default</HTML>");
            fileContainer.add(resetPathBtn, BorderLayout.EAST);
            resetPathBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            resetPathBtn.setOpaque(true);
            resetPathBtn.setToolTipText("Reset the save file path to the default stored in the same folder as this program.");

            mainMenu.add(fileContainer);

            createBtn(fileBtn, "Change save file", mainMenu);
            fileBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
            fileBtn.setToolTipText("Change the file path for the save data file.");

            container.add(mainMenu, BorderLayout.WEST);

            createLabel(about, "", container, labelFont);

            frame.add(container);
            frame.setVisible(true);

            btnContainer.setLayout(new GridLayout(2,2, 2, 2));
            btnContainer.add(greenBtn);
            btnContainer.add(redBtn);
            btnContainer.add(yellowBtn);
            btnContainer.add(blueBtn);

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

                    fileSetup();
                }

                fileFrame.dispose();
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

                about.setText("<HTML>Welcome to Simon.<br><br>Your hi-score is " + hiScore + ".<br><br>How to play:<br>There are four buttons – green, red, yellow and blue. When one button lights up, click on the same one. The sequence begins with a single flash, followed by a repeat of the previous button and a quick flash of a different button. Your task is to replicate this pattern. The complexity increases with each successful round, adding an extra flash each time. The game continues until an incorrect pattern repetition occurs.<br><br>Based on the game by <u>Ralph H. Baer and Howard J. Morrison</u>.<br>Developed by <u>Kiefer Menard</u>.<br><br>Save file path: <strong>" + (file.getPath().equals("./simon.txt") ? "Default" : "<em>" + file.getPath() + "</em>") + "</strong></HTML>");
            });

            resetPathBtn.addActionListener(e -> {
                file = new File("./simon.txt");
                fileSetup();
            });

            fileBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "<HTML>Please select a text file or directory for storing save data.<br>If a file isn't chosen, a default text file called <em>simon.txt</em><br>will be generated in the selected directory.</HTML>", "Choose File", JOptionPane.PLAIN_MESSAGE);
                fileFrame.setVisible(true);
            });

            greenBtn.addActionListener(e -> {
                btnPressed.set(true);
                input.set(1);
            });
            redBtn.addActionListener(e -> {
                btnPressed.set(true);
                input.set(2);
            });
            yellowBtn.addActionListener(e -> {
                btnPressed.set(true);
                input.set(3);
            });
            blueBtn.addActionListener(e -> {
                btnPressed.set(true);
                input.set(4);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }

    /**
     * The playGame method runs the program flow once the game is running.
     *
     * @author Kiefer Menard
     */
    private void playGame() {
        // Variables
        score = 0; // Reset score
        pattern = new ArrayList<>();

        container.removeAll();
        container.add(btnContainer);
        container.revalidate();
        container.repaint();

        executor.execute(runnable);
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

        container.add(mainMenu, BorderLayout.WEST);
        container.add(about);

        hiScore = max(score, hiScore);
        writeFile();

        about.setText("<HTML>Welcome to Simon.<br><br>Your hi-score is " + hiScore + ".<br><br>How to play:<br>There are four buttons – green, red, yellow and blue. When one button lights up, click on the same one. The sequence begins with a single flash, followed by a repeat of the previous button and a quick flash of a different button. Your task is to replicate this pattern. The complexity increases with each successful round, adding an extra flash each time. The game continues until an incorrect pattern repetition occurs.<br><br>Based on the game by <u>Ralph H. Baer and Howard J. Morrison</u>.<br>Developed by <u>Kiefer Menard</u>.<br><br>Save file path: <strong>" + (file.getPath().equals("./simon.txt") ? "Default" : "<em>" + file.getPath() + "</em>") + "</strong></HTML>");
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
                bgColor = new Color(parseInt(scanner.nextLine()));
                refreshColors();
                hiScore = parseInt(scanner.nextLine());
            } catch (Exception ex) {
                System.out.println("Incorrect file data.");
            }

            about.setText("<HTML>Welcome to Simon.<br><br>Your hi-score is " + hiScore + ".<br><br>How to play:<br>There are four buttons – green, red, yellow and blue. When one button lights up, click on the same one. The sequence begins with a single flash, followed by a repeat of the previous button and a quick flash of a different button. Your task is to replicate this pattern. The complexity increases with each successful round, adding an extra flash each time. The game continues until an incorrect pattern repetition occurs.<br><br>Based on the game by <u>Ralph H. Baer and Howard J. Morrison</u>.<br>Developed by <u>Kiefer Menard</u>.<br><br>Save file path: <strong>" + (file.getPath().equals("./simon.txt") ? "Default" : "<em>" + file.getPath() + "</em>") + "</strong></HTML>");

            scanner.close();
        } catch (IOException ex) {
           ex.printStackTrace();
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
            ex.printStackTrace();
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
     * The nativeKeyTyped method handles keyboard inputs.
     *
     * @author Kiefer Menard
     */
    public void nativeKeyTyped(NativeKeyEvent e) {}
    /**
     * The nativeKeyPressed method handles key presses on the keyboard.
     *
     * @author Kiefer Menard
     */
    public void nativeKeyPressed(NativeKeyEvent e) {}

    /**
     * The nativeKeyReleased method handles key releases on the keyboard.
     *
     * @author Kiefer Menard
     */
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == 1) {
            if (greenBtn.isValid()) {
                endGame();
            }
        }
    }

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
     * The pause method effectively pauses the method that calls it until the specified duration is up.
     *
     * @param duration The duration of time to wait in milliseconds.
     * @author Kiefer Menard
     */

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