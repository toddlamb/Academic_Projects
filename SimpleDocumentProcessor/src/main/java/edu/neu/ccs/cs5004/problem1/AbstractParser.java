package edu.neu.ccs.cs5004.problem1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toddl on 4/2/2017.
 */
abstract class AbstractParser implements Parser {
  protected Pattern parsePattern;

  /**
   * Creates a new abstract parser with a parser pattern.
   *
   * @param parsePattern The pattern used to parse
   */
  protected AbstractParser(Pattern parsePattern) {
    this.parsePattern = parsePattern;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    AbstractParser that = (AbstractParser) object;

    return getParsePattern() != null
               ? getParsePattern().equals(that.getParsePattern())
               : that.getParsePattern() == null;

  }

  @Override
  public int hashCode() {
    return getParsePattern() != null
               ? getParsePattern().hashCode() : 0;
  }

  @Override
  public String toString() {
    return "AbstractParser{" + "parsePattern=" + parsePattern + '}';
  }

  /**
   * Gets the pattern used to parse the text.
   *
   * @return the pattern used to parse the text.
   */
  protected Pattern getParsePattern() {
    return parsePattern;
  }

  public Boolean isEligible(String line) {
    Matcher parseMatcher = parsePattern.matcher(line);
    return parseMatcher.find();
  }


}
