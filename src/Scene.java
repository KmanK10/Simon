import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
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
    private int score = 0;
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
            fileFrame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
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

            mainMenu.setLayout(new GridLayout(5, 1));

            createLabel(title, "Simon", mainMenu, H1);

            createBtn(startBtn, "Play", mainMenu);

            createLabel(optionLabel, "Options", mainMenu, H2);

            createBtn(bgBtn, "Change background color", mainMenu);
            bgBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

            createBtn(fileBtn, "Change save file", mainMenu);
            fileBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

            container.add(mainMenu, BorderLayout.WEST);

            createLabel(about, "", container, labelFont);

            frame.add(container);

            greenBtn.setBackground(Color.GREEN.darker());
            greenBtn.setOpaque(true);
            redBtn.setBackground(Color.RED.darker());
            redBtn.setOpaque(true);
            yellowBtn.setBackground(Color.YELLOW.darker());
            yellowBtn.setOpaque(true);
            blueBtn.setBackground(Color.BLUE.darker());
            blueBtn.setOpaque(true);

            if (file == null) {
                JOptionPane.showMessageDialog(null, "<HTML>Please select a text file or directory for storing save data.<br>If a file isn't chosen, a default text file called <em>simon.txt</em><br>will be generated in the selected directory.</HTML>", "Choose File", JOptionPane.PLAIN_MESSAGE);
                fileFrame.setVisible(true);
            }

            chooser.addActionListener(e -> {
                File tempFile = chooser.getSelectedFile();

                if (tempFile != null && (tempFile.getName().indexOf('.') == -1 || tempFile.getName().substring(tempFile.getName().indexOf('.')).equals(".txt"))) {
                    if (e.getActionCommand().equals("ApproveSelection")) {
                        file = tempFile;
                    }

                    if (file != null) {
                        fileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        fileFrame.dispose();

                        frame.setVisible(true);
                        System.out.println(container.requestFocusInWindow());

                        fileSetup();
                    }
                }
            });
            startBtn.addActionListener(e -> playGame());
            bgBtn.addActionListener(e -> {
                picker.setColor(bgColor);
                colorFrame.setVisible(true);
            });
            fileBtn.addActionListener(e -> fileFrame.setVisible(true));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void playGame() {
        score++;

        container.removeAll();
        container.revalidate();
        container.repaint();

        container.setLayout(new GridLayout(2,2));
        container.add(greenBtn);
        container.add(yellowBtn);
        container.add(redBtn);
        container.add(blueBtn);
    }

    private void endGame() {
        container.removeAll();
        container.revalidate();
        container.repaint();

        hiScore = max(score, hiScore);

        initialize();
        fileSetup();
    }

    private void fileSetup() {
        // Variables:
        String line = "";

        try {
            if (file.isDirectory()) {
                file = new File(file.getPath() + File.separator + "simon.txt");
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

            about.setText("<HTML>Welcome to Simon.<br><br>Your hi-score is " + hiScore + ".<br><br>How to play:<br>There are four buttons — green, red, yellow and blue. When one button lights up, click on the same one. The sequence begins with a single flash, followed by a repeat of the previous button and a quick flash of a different button. Your task is to replicate this pattern. The complexity increases with each successful round, adding an extra flash each time. The game continues until an incorrect pattern repetition occurs.<br><br>Developed by Kiefer Menard.<br><br>Save file path: <em>" + (file != null ? file.getPath() : "null") + "</em></HTML>");

            scanner.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeFile() {
        try {
            writer = new FileWriter(file);

            writer.write(bgColor.getRGB() + "\n" + hiScore);

            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

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
        fileBtn.setBackground(bgAdjusted);
        about.setBackground(bgColor);
        about.setForeground(textColor);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public void stateChanged(ChangeEvent e) {
        if (picker.getColor().getAlpha() == 255) {
            bgColor = picker.getColor();
            writeFile();
            refreshColors();
        }
    }

    private JButton createBtn(JButton button, String text, JPanel panel) {
        button.setText(text);
        button.setFont(btnFont);
        button.setOpaque(true);

        panel.add(button);

        return button;
    }

    private JLabel createLabel(JLabel label, String text, JPanel panel, Font font) {
        label.setText(text);
        label.setFont(font);

        panel.add(label);

        return label;
    }
}