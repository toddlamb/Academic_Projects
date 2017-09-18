package edu.neu.ccs.cs5004;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Todd on 4/12/2017.
 */
public class GameStateTest {
  GameState gameState;

  @Before
  public void setUp() throws Exception {
    gameState = new GameState(new File("word.txt"));
  }

  @Test
  public void getGameName() throws Exception {
    assertEquals("Hangman", GameState.getGameName());
  }

  @Test
  public void getGuessesLeft() throws Exception {
    assertEquals(6, gameState.getGuessesLeft());
  }

  @Test
  public void setGuessesLeft() throws Exception {
    gameState.setGuessesLeft(5);
    assertEquals(5, gameState.getGuessesLeft());
  }

  @Test
  public void getSecretWord() throws Exception {
    assertEquals("testing", gameState.getSecretWord());
  }

  @Test
  public void setSecretWord() throws Exception {
    gameState.setSecretWord("test");
    assertEquals("test", gameState.getSecretWord());
  }

  @Test
  public void newSecretWord() throws Exception {
    gameState.newSecretWord();
    assertEquals("testing", gameState.getSecretWord());
  }

  @Test
  public void gameWon() throws Exception {
    assertFalse(gameState.gameWon());
    for (char c : gameState.getSecretWord().toCharArray()) {
      gameState.getGameLetters().addGuess(c);
    }
    assertTrue(gameState.gameWon());
  }

  @Test
  public void gameLost() throws Exception {
    assertFalse(gameState.gameLost());
    for (char c = 'a'; c <= 'h'; c++) {
      gameState.processGuess(c);
    }
    assertTrue(gameState.gameLost());
  }

  @Test
  public void equals() throws Exception {

  }

  @Test
  public void hashCodeTest() throws Exception {

  }
}