# Simon Game

## Overview
Simon Game is a Java-based implementation of the classic memory game "Simon." The program features a graphical user interface (GUI) built with Swing, where players replicate an increasingly complex sequence of colored button flashes. The game includes options to customize the background color, save high scores, and manage save file paths, with data persistence in a text file.

## Features
- **Gameplay**: Players memorize and repeat a sequence of flashes from four colored buttons (green, red, yellow, blue), with the sequence growing longer each round.
- **Graphical Interface**: A Swing-based GUI with a main menu, customizable background color, and a game panel displaying the four buttons.
- **Score Tracking**: Tracks the current score and high score, saved to a text file (`simon.txt` by default).
- **Customization**: Allows users to change the background color via a color picker and select a custom save file or directory.
- **Save Data Management**: Options to reset save data (color and high score) or revert to the default save file path.
- **Keyboard Support**: Includes basic keyboard input handling using JNativeHook (e.g., Esc key to end the game).

## Usage
1. **Prerequisites**: Ensure Java is installed and the `jnativehook` library (version 2.2.2 or compatible) is included in the project.
2. **Running the Program**:
   - Compile and run `Main.java`.
   - The GUI opens with a main menu displaying the game title, play button, and options.
3. **Playing the Game**:
   - Click "Play" to start. Watch the sequence of button flashes and click the buttons in the same order.
   - The game ends if an incorrect button is pressed, displaying the score and updating the high score if applicable.
   - Return to the main menu to play again.
4. **Customization**:
   - Click "Change background color" to open a color picker and select a new background color.
   - Click "Change save file" to choose a new text file or directory for saving data.
   - Use "Reset save data" to restore the default background (white) and clear the high score.
   - Use "Reset file path" to revert to the default save file (`simon.txt`).

## File Structure
- `Main.java`: Entry point that initializes and configures the `Scene` class.
- `Scene.java`: Core class handling the GUI, game logic, file I/O, and user interactions.
- `simon.txt`: Default text file for storing the background color (as an RGB integer) and high score.

## Example Save File (`simon.txt`)
```
-1
5
```
- First line: RGB value of the background color (e.g., `-1 `

## Dependencies
- Java Standard Library (including `javax.swing` for GUI)
- JNativeHook (`com.github.kwhat.jnativehook`) for keyboard input handling

## Contributing
Contributions are welcome! Please open an issue or submit a pull request with enhancements, bug fixes, or features like sound effects, improved keyboard controls, or multiplayer support.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.
