package edu.neu.ccs.cs5004.problem1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parser for enumeration lists in a document.
 * Created by Todd on 4/1/2017.
 */
class EnumerationListParser extends AbstractParserWithList {
  private static final int START_OF_ALPHABET = 96;
  private static final int END_OF_ALPHABET = 27;
  private static final int SIZE_OF_ALPHABET = 26;

  /**
   * Creates a new enumeration list parser.
   */
  protected EnumerationListParser() {
    super(Pattern.compile("^(\\s*)(1\\.|\\.)(?=\\s)"));
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return "EnumerationListParser{} " + super.toString();
  }

  @Override
  public String parseLine(String line) {
    Matcher parseMatcher = getParsePattern().matcher(line);
    parseMatcher.find();
    Integer nestingCount = nestingLevel(line);
    if (nestingLevel(line) > getNestLevelCounter().size()) {
      for (int i = nestingCount - getNestLevelCounter().size(); i > 0; i--) {
        getNestLevelCounter().add(0);
      }
    } else {
      resetLessSignif(nestingCount);
    }
    getNestLevelCounter().set(nestingCount - 1, getNestLevelCounter().get(nestingCount - 1) + 1);
    Integer countAtLevel = getNestLevelCounter().get(nestingCount - 1);
    if (isEven(nestingCount - 1)) {
      return parseMatcher.replaceFirst(parseMatcher.group(1) + countAtLevel + ".");
    } else {
      return parseMatcher.replaceFirst(parseMatcher.group(1) + numHeaderToStrHeader(
          countAtLevel) + ""
                                           + ".");
    }

  }

  /**
   * Determines the level of the line in the list containing it.
   *
   * @param line The non-null line being evaluated.
   *
   * @return The level of the line in the list containing it where the parent level is one.
   */
  protected Integer nestingLevel(String line) {
    Pattern parsePattern = Pattern.compile("\\s*");
    Matcher parseMatcher = parsePattern.matcher(line);
    parseMatcher.find();
    return parseMatcher.group().length() / 2 + 1;
  }

  protected void resetLessSignif(int nestingLevel) {
    for (int i = nestingLevel; i < getNestLevelCounter().size(); i++) {
      getNestLevelCounter().set(i, 0);
    }
  }

  /**
   * Determines an alphabetical list header based on an equivalent numerical heading value. Once
   * the given number exceeds the number of characters in the alphabet, adds necessary character
   * positions.
   * Examples
   * 1 returns "a"
   * 2 returns "b"
   * 27 returns "aa"
   *
   * @param number The non-null numerical value corresponding to our desired output as a character.
   *
   * @return The character equivalent of the numerical list header value.
   */
  protected String getCharForNumber(int number) {
    return number > 0 && number < END_OF_ALPHABET
               ? String.valueOf((char) (number + START_OF_ALPHABET)) : null;
  }

  /**
   * Determines if a number is even or odd.
   *
   * @param num The non-null number being evaluated.
   *
   * @return True if the number is even and false if it is odd.
   */
  private boolean isEven(Integer num) {
    return num % 2 == 0;
  }

  /**
   * Determines an alphabetical list header based on an equivalent numerical heading value. Once
   * the given number exceeds the number of characters in the alphabet, adds necessary character
   * positions using base 26 counting.
   * Examples
   * 1 returns "a"
   * 2 returns "b"
   * 27 returns "aa"
   *
   * @param headerNumber The non-null numberical header value corresponding to the desired output.
   *
   * @return The string representation of the numerical header value supplied.
   */
  protected String numHeaderToStrHeader(Integer headerNumber) {
    StringBuffer res = new StringBuffer();
    String convertToBase26 = Integer.toString(headerNumber, SIZE_OF_ALPHABET);
    char[] numberArray = convertToBase26.toCharArray();
    for (char number : numberArray) {
      res.append(getCharForNumber(Character.getNumericValue(number)));
    }
    return res.toString();
  }


}
