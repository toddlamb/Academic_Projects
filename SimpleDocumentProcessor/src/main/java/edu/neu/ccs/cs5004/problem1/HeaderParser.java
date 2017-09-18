package edu.neu.ccs.cs5004.problem1;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parser for header text injection in our word processor.
 * Created by toddl on 3/31/2017.
 */
public class HeaderParser extends AbstractParserWithList {

  protected HeaderParser() {
    super(Pattern.compile("^#+"));
  }

  public HeaderParser(Pattern parsePattern, List<Integer> nestLevelCounter) {
    super(parsePattern, nestLevelCounter);
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
    return "HeaderParser{} " + super.toString();
  }

  @Override
  public String parseLine(String line) {
    Matcher parseMatcher = getParsePattern().matcher(line);
    parseMatcher.find();
    Integer hashCount = parseMatcher.group().length();
    if (hashCount > getNestLevelCounter().size()) {
      for (int i = (hashCount - getNestLevelCounter().size()); i > 0; i--) {
        getNestLevelCounter().add(1);
      }
    } else {
      getNestLevelCounter().set(hashCount - 1, getNestLevelCounter().get(hashCount - 1) + 1);
      resetLessSignif(hashCount);
    }
    StringBuffer replacement = new StringBuffer();
    Integer count = 0;
    while (count + 1 < getNestLevelCounter().size()) {
      replacement.append(getNestLevelCounter().get(count++) + ".");
    }
    replacement.append(getNestLevelCounter().get(count));
    return line.replaceFirst("^#+", replacement.toString());
  }


  protected void resetLessSignif(int nestingLevel) {
    int origListSize = getNestLevelCounter().size();
    for (int i = nestingLevel; i < origListSize; i++) {
      getNestLevelCounter().remove(nestingLevel);
    }
  }

}
