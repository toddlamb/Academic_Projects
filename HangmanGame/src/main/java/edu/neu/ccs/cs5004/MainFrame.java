package edu.neu.ccs.cs5004;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.nio.file.FileSystems;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.*;

/**
 * Represents the view of this application.
 * Created by toddl on 4/8/2017.
 */
public class MainFrame extends JFrame {
  private static final int PIC_SPACING = 10;
  private static final String STARTING_IMAGE = "Images" + FileSystems.getDefault().getSeparator()
                                                   + "Hangman" + GameState.STARTING_LIVES + ".png";
  private static final String NEW_BUTTON_CONTENT = "New";
  private Picture gamePicture;
  private GameText gameText;

  /**
   * Creates a new custom swing frame.
   *
   * @param gameState The model to be used with the view.
   */
  protected MainFrame(GameState gameState) {
    super(GameState.getGameName());
    Controller controller = new Controller(gameState);
    setFrameDefaults(this, controller);
    Container container = this.getContentPane();
    this.gamePicture = new Picture(STARTING_IMAGE);
    this.gameText = new GameText(gameState);
    addComponents(container, controller);
    this.pack();
    this.setVisible(true);
    this.requestFocus();
  }

  /**
   * Gets the game picture in the GUI.
   *
   * @return the game picture in the GUI.
   */
  private Picture getGamePicture() {
    return gamePicture;
  }

  /**
   * Gets the text-related components of the GUI.
   *
   * @return the text-related components of the GUI.
   */
  private GameText getGameText() {
    return gameText;
  }

  /**
   * Adjusts the GUI frame's main settings.
   *
   * @param frame      The GUI frame.
   * @param controller The view controller for this GUI frame.
   */
  private void setFrameDefaults(MainFrame frame, Controller controller) {
    frame.setSize(500, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    frame.setBackground(Color.WHITE);
    frame.addKeyListener(controller);
  }

  /**
   * Adds game components to frame.
   *
   * @param container  The container of the GUI frame.
   * @param controller The view controller of the GUI frame.
   */
  private void addComponents(Container container, Controller controller) {
    addMainPanel(container);
    addGameText(container);
    buttonAdd(container, controller);
  }

  /**
   * Adds a new game button to a frame container.
   *
   * @param container  The container of a GUI frame.
   * @param controller The view controller of the GUI frame.
   */
  private void buttonAdd(Container container, Controller controller) {
    JButton button = new JButton(NEW_BUTTON_CONTENT);
    button.addActionListener(controller);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setFocusable(false);
    container.add(button);
  }

  /**
   * Adds text-related game components to a frame container.
   *
   * @param container The container of a GUI frame.
   */
  private void addGameText(Container container) {
    container.add(getGameText().getSecretWord(), BorderLayout.CENTER);
    container.add(getGameText().getGameStatus());
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    container.add(panel);
    for (JLabel letter : getGameText().getLetterGrid()) {
      panel.add(letter);
    }
  }

  /**
   * Adds the main panel containing the game image to the frame container.
   *
   * @param container The frame container of the GUI.
   */
  private void addMainPanel(Container container) {
    JPanel mainPanel = new JPanel();
    mainPanel.add(Box.createRigidArea(new Dimension(0, PIC_SPACING)));
    mainPanel.add(getGamePicture());
    container.add(mainPanel);
  }

  /**
   * Represents a picture swing component of a swing application.
   * Created by toddl on 4/8/2017.
   */
  private static class Picture extends JLabel {
    /**
     * Creates a new picture swing component.
     *
     * @param imageFile The location of the image file used for the picture.
     */
    private Picture(String imageFile) {
      super(new ImageIcon(imageFile), SwingConstants.CENTER);
    }
  }

  /**
   * Represents the text-related components of the application.
   */
  private static class GameText {
    private JLabel gameStatus;
    private JLabel secretWord;
    private JLabel[] letterGrid;

    /**
     * Creates text-related components of the application based on a game-state.
     *
     * @param gameState The model of the application.
     */
    private GameText(GameState gameState) {
      this.gameStatus = new JLabel(gameState.getGuessesLeft()
                                       + " guesses left", SwingConstants.CENTER);
      getGameStatus().setAlignmentX(CENTER_ALIGNMENT);
      getGameStatus().setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
      this.letterGrid = new JLabel[GameState.GameLetters.getAlphabet().size()];
      this.secretWord = new JLabel(gameState.getSecretWord().replaceAll(".", "?"), SwingConstants
                                                                                       .CENTER);
      secretWord.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
      secretWord.setAlignmentX(CENTER_ALIGNMENT);
      for (int i = 0; i < GameState.GameLetters.getAlphabet().size(); i++) {
        letterGrid[i] = new JLabel(GameState.GameLetters.getAlphabet().get(i).toString());
        letterGrid[i].setForeground(Color.LIGHT_GRAY);
        letterGrid[i].setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
      }
    }

    /**
     * Gets the component representing the text-oriented status of the game.
     *
     * @return the component representing the text-oriented status of the game.
     */
    private JLabel getGameStatus() {
      return gameStatus;
    }

    /**
     * Gets the component representing the game's letter grid.
     *
     * @return the component representing the game's letter grid.
     */
    private JLabel[] getLetterGrid() {
      return letterGrid;
    }

    /**
     * Gets the component representing the game's secret word.
     *
     * @return the component representing the game's secret word.
     */
    private JLabel getSecretWord() {
      return secretWord;
    }
  }

  /**
   * Represents a view controller.
   * Created by toddl on 4/9/2017.
   */
  private static class Controller implements KeyListener, ActionListener {
    private GameState gameState;

    /**
     * Creates a new controller for a view.
     *
     * @param gameState the model in use for this application.
     */
    private Controller(GameState gameState) {
      this.gameState = gameState;
    }

    public GameState getGameState() {
      return gameState;
    }

    @Override
    public void keyTyped(KeyEvent event) {
      Character letter = event.getKeyChar();
      if (Character.isLetter(letter)
              && !getGameState().gameLost()
              && !getGameState().gameWon()
              && getGameState().getGameLetters().addGuess(letter)) {
        getGameState().processGuess(letter);
      }
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      getGameState().setGuessesLeft(GameState.STARTING_LIVES);
      getGameState().getGameLetters().getGuessedLetters().clear();
      getGameState().newSecretWord();
    }

  }

  /**
   * Represents part of the view that allows components to be rendered and re-rendered due to
   * changes.
   * Created by toddl on 4/10/2017.
   */
  private class ViewRenderer implements Observer {
    private ViewRenderer() {
    }

    @Override
    public void update(Observable observable, Object arg) {
      try {
        GameState gameState = (GameState) observable;
        renderLetters(gameState);
        renderPicture(gameState);
        renderSecretWord(gameState);
        renderGameStatus(gameState);
        repaint();
      } catch (ClassCastException exception) {
        return;
      }
    }

    /**
     * Updates the letter grid of the GUI and re-renders it on the GUI.
     *
     * @param gameState The model used to render the grid.
     */
    private void renderLetters(GameState gameState) {
      JLabel[] letterGrid = MainFrame.this.getGameText().getLetterGrid();
      Set<Character> guessLetters = gameState.getGameLetters().getGuessedLetters();
      for (JLabel label : letterGrid) {
        Character letter = label.getText().toCharArray()[0];
        if (guessLetters.contains(letter)) {
          label.setForeground(Color.BLUE);
        } else {
          label.setForeground(Color.lightGray);
        }
      }
    }

    /**
     * Updates the game picture of the GUI and re-renders it on the GUI.
     *
     * @param gameState The model used to render the picture.
     */
    private void renderPicture(GameState gameState) {
      String filePath = "Images" + FileSystems.getDefault().getSeparator()
                            + GameState.getGameName() + gameState.getGuessesLeft() + ".png";
      MainFrame.this.getGamePicture().setIcon(new ImageIcon(filePath));
    }

    /**
     * Updates the secret word component of the GUI and re-renders it on the GUI.
     *
     * @param gameState The model used to render the component.
     */
    private void renderSecretWord(GameState gameState) {
      JLabel secretWordLabel = MainFrame.this.getGameText().getSecretWord();
      char[] actualWord = gameState.getSecretWord().toCharArray();
      Set<Character> guessedLetters = gameState.getGameLetters().getGuessedLetters();
      char[] presentedWord = secretWordLabel.getText().toCharArray();
      for (int i = 0; i < actualWord.length; i++) {
        if (guessedLetters.contains(actualWord[i])) {
          presentedWord[i] = actualWord[i];
        } else {
          presentedWord[i] = '?';
        }
      }
      secretWordLabel.setText(String.valueOf(presentedWord));
    }

    /**
     * Updates the game status component of the GUI and re-renders it on the GUI.
     *
     * @param gameState The model used to render the game status.
     */
    private void renderGameStatus(GameState gameState) {
      JLabel gameStatus = MainFrame.this.getGameText().getGameStatus();
      if (gameState.gameLost()) {
        gameStatus.setText("You lost! (" + gameState.getSecretWord() + ")");
      } else if (gameState.gameWon()) {
        gameStatus.setText("You win with " + gameState.getGuessesLeft() + " guesses left");
      } else {
        gameStatus.setText(gameState.getGuessesLeft() + " guesses left");
      }
    }
  }

  /**
   * Initializes the GUI on the swing thread.
   *
   * @param args command-line arguments.
   */
  public static void main(String[] args) {
    GameState gameState = new GameState(new File("words.txt"));
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainFrame mainFrame = new MainFrame(gameState);
        gameState.addObserver(mainFrame.new ViewRenderer());
      }
    });
  }
}
