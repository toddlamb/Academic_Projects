package edu.neu.ccs.cs5004.problem1;

/**
 * Represents an object that parses and modifies text data in a meaningful way.
 * Created by toddl on 3/31/2017.
 */
public interface Parser {
  /**
   * Parses a line of text, modifies it, and returns the output.
   *
   * @param line The non-null line of text fed into the parser.
   *
   * @return The output text of the parser.
   */
  String parseLine(String line);

  /**
   * Determines if a line of text meets the criteria to be properly parsed.
   *
   * @param line The non-null line of text being evaluated.
   *
   * @return True if the line is eligible to be parsed by this parser and false otherwise.
   */
  Boolean isEligible(String line);
}
