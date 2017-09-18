package edu.neu.ccs.cs5004.problem1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parser for itemization list lines.
 * Created by Todd on 4/1/2017.
 */
class ItemizationListParser extends AbstractParser {

  protected ItemizationListParser() {
    super(Pattern.compile("^\\s*[*+-]"));
  }

  /**
   * A constructor used for testing purposes only.
   *
   * @param parsePattern A pattern used for parsing text.
   */
  public ItemizationListParser(Pattern parsePattern) {
    super(parsePattern);
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
    return "ItemizationListParser{} " + super.toString();
  }

  @Override
  public String parseLine(String line) {
    Matcher parseMatcher = getParsePattern().matcher(line);
    parseMatcher.find();
    char[] lineChars = line.toCharArray();
    lineChars[parseMatcher.end() - 1] = '*';
    return new String(lineChars);
  }

  /**
   * Determines the level of the line in the list containing it.
   *
   * @param line The non-null line being evaluated.
   *
   * @return The level of the line in the list containing it where the parent level is one.
   */
  protected Integer nestingLevel(String line) {
    Pattern parsePattern = Pattern.compile("^\\s*");
    Matcher parseMatcher = parsePattern.matcher(line);
    parseMatcher.find();
    if (parseMatcher.group().length() == 0) {
      return 1;
    } else {
      return parseMatcher.group().length() / 2 + 1;
    }
  }

}
