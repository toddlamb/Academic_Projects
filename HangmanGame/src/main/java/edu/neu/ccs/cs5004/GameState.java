package edu.neu.ccs.cs5004;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

/**
 * Represents the state of the game model.
 * Created by toddl on 4/8/2017.
 */
class GameState extends Observable {
  protected static final int STARTING_LIVES = 6;
  private static final String GAME_NAME = "Hangman";
  private int guessesLeft;
  private String secretWord;
  private GameLetters gameLetters;
  private WordBank wordBank;

  /**
   * Creates a new state for the game model
   *
   * @param file A filepath corresponding to a text file of words.
   */
  public GameState(File file) {
    this.guessesLeft = STARTING_LIVES;
    this.wordBank = this.new WordBank(file);
    this.secretWord = wordBank.getNextWord();
    this.gameLetters = new GameLetters();
  }

  /**
   * Gets the name of the game.
   *
   * @return the name of the game.
   */
  protected static String getGameName() {
    return GAME_NAME;
  }


  /**
   * Gets the guesses left in the game.
   *
   * @return the guesses left in the game.
   */
  protected int getGuessesLeft() {
    return guessesLeft;
  }

  /**
   * Sets the non-null guesses left in the game.
   *
   * @param guessesLeft Guesses left in the game.
   */
  protected void setGuessesLeft(int guessesLeft) {
    this.guessesLeft = guessesLeft;
  }

  /**
   * Gets the null secret word in the game.
   *
   * @return the secret word in the game.
   */
  protected String getSecretWord() {
    return secretWord;
  }

  /**
   * Gets the non-null secret word in the game.
   *
   * @param secretWord the non-null secret word in the game.
   */
  protected void setSecretWord(String secretWord) {
    this.secretWord = secretWord;
  }

  /**
   * Gets the collection of text letters relevant to the game.
   *
   * @return the collection of text letters relevant to the game.
   */
  protected GameLetters getGameLetters() {
    return gameLetters;
  }

  /**
   * Adds a guessed letter to the word bank, adjusts lives if guess missed secret word, and calls
   *     the view rendered to update the GUI.
   *
   * @param letter A non-null guessed letter by the user.
   */
  protected void processGuess(Character letter) {
    if (!secretWord.contains(letter.toString())) {
      setGuessesLeft(getGuessesLeft() - 1);
    }
    setChanged();
    notifyObservers();
  }

  /**
   * Retrieves a new secret word from the word bank and calls the view renderer.
   */
  protected void newSecretWord() {
    setSecretWord(wordBank.getNextWord());
    setChanged();
    notifyObservers();
  }

  /**
   * Determines if the game is won by the user.
   *
   * @return True if the game is won and false otherwise.
   */
  protected boolean gameWon() {
    Set<Character> guessLetters = getGameLetters().getGuessedLetters();
    for (char letter : getSecretWord().toCharArray()) {
      if (!guessLetters.contains(letter)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines if the game is lost by the user.
   *
   * @return True if the game is lost and false otherwise.
   */
  protected boolean gameLost() {
    return getGuessesLeft() == 0;
  }

  /**
   * Represents a collection of secret words available for use in the game.
   *     Created by toddl on 4/8/2017.
   */
  class WordBank {
    private List<String> gameWords;
    private Random randomizer;

    /**
     * Creates a new wordbank for use in the game.
     *
     * @param filePath The non-null file path of the file containing words for the game.
     */
    private WordBank(File filePath) {
      this.gameWords = new WordBankReader(filePath).writeTo();
      this.randomizer = new Random();
    }

    /**
     * Gets the next word from the word bank for use in the game.
     *
     * @return the next word from the word bank for use in the game.
     */
    private String getNextWord() {
      return gameWords.get(Math.abs(randomizer.nextInt()) % gameWords.size());
    }

    /**
     * Represents a file reader used to read words in a file to memory.
     */
    private class WordBankReader implements TextReader {
      private BufferedReader wordReader;

      /**
       * Creates a new word bank reader.
       *
       * @param filePath The non-null file read by the reader.
       */
      private WordBankReader(File filePath) {
        try {
          this.wordReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),
                                                                        "UTF-8"));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

      /**
       * Reads a line from the word text.
       *
       * @return a line from the word text.
       */
      @Override
      public String readFromFile() {
        String result = null;
        try {
          result = wordReader.readLine();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return result;
      }

      /**
       * Writes the contents of a text file to a list.
       *
       * @return the contents of a text file as a list.
       */
      @Override
      public List<String> writeTo() {
        List<String> result = new LinkedList<>();
        String word;
        while ((word = readFromFile()) != null) {
          result.add(word);
        }
        try {
          wordReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return result;
      }
    }


  }

  /**
   * Represents a collection of assorted letters relevant to the game.
   *     Created by toddl on 4/8/2017.
   */
  static class GameLetters {
    private static List<Character> alphabet = new ArrayList<Character>();
    private Set<Character> guessedLetters;

    /**
     * Create alphabet.
     */
    static {
      for (char letter = 'a'; letter <= 'z'; letter++) {
        alphabet.add(letter);
      }
    }

    /**
     * Creates a new game letter object.
     */
    private GameLetters() {
      this.guessedLetters = new HashSet<>();
    }

    /**
     * Adds a user guess to the game letters.
     *
     * @param letter The non-null user's guess.
     *
     * @return True if the guess is unique and false otherwise.
     */
    protected Boolean addGuess(Character letter) {
      return guessedLetters.add(letter);
    }

    /**
     * Gets the alphabet.
     *
     * @return the alphabet.
     */
    protected static List<Character> getAlphabet() {
      return alphabet;
    }

    /**
     * Gets the collection of user guessed letters.
     *
     * @return the collection of user guessed letters.
     */
    protected Set<Character> getGuessedLetters() {
      return guessedLetters;
    }
  }
}
