package edu.neu.ccs.cs5004.problem1;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by toddl on 4/2/2017.
 */
abstract class AbstractParserWithList extends AbstractParser {
  protected List<Integer> nestLevelCounter;

  /**
   * Creates an abstract list parser with a pattern and a list.
   *
   * @param parsePattern The pattern used to parse the data.
   */
  protected AbstractParserWithList(Pattern parsePattern) {
    super(parsePattern);
    this.nestLevelCounter = new ArrayList<Integer>();
  }

  /**
   * A constructor used for testing purposes only.
   *
   * @param parsePattern     A pattern used for parsing text.
   * @param nestLevelCounter A list used to keep track of nesting levels for document lists.
   */
  protected AbstractParserWithList(Pattern parsePattern, List<Integer> nestLevelCounter) {
    super(parsePattern);
    this.nestLevelCounter = nestLevelCounter;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    if (!super.equals(object)) {
      return false;
    }

    AbstractParserWithList that = (AbstractParserWithList) object;

    return getNestLevelCounter().equals(that.getNestLevelCounter());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getNestLevelCounter().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "AbstractParserWithList{nestLevelCounter=" + nestLevelCounter + "} " + super.toString();
  }

  /**
   * Gets the list that keeps track of nesting levels for a given list in a document.
   *
   * @return the list that keeps track of nesting levels for a given list in a document.
   */
  protected List<Integer> getNestLevelCounter() {
    return nestLevelCounter;
  }

  /**
   * Takes an index of a list and sets all less significant values i.e. subsequent values to zero.
   *
   * @param nestingLevel The non-null index of the list we are modifying.
   */
  abstract protected void resetLessSignif(int nestingLevel);


}


