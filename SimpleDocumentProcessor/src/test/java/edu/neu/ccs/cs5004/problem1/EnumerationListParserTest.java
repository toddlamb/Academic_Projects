package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by toddl on 4/2/2017.
 */
public class EnumerationListParserTest {
  EnumerationListParser parser, parser2;
  @Before
  public void setUp() throws Exception {
    parser = new EnumerationListParser();
    parser2 = new EnumerationListParser();
  }

  @Test
  public void parseLine() throws Exception {
    assertEquals("1. Item, nesting level 1",parser.parseLine("1. Item, nesting level 1"));
    assertEquals("2. Item, nesting level 1",parser.parseLine("1. Item, nesting level 1"));
    assertEquals("  a. Item, nesting level 2",parser.parseLine("  1. Item, nesting level 2"));
    assertEquals("  b. Item, nesting level 2",parser.parseLine("  1. Item, nesting level 2"));
    assertEquals("3. Item, nesting level 1",parser.parseLine("1. Item, nesting level 1"));
    assertEquals("  a. Item, nesting level 2",parser.parseLine("  1. Item, nesting level 2"));
  }

  @Test
  public void parsingWithMixedListTypes() throws Exception {
    assertEquals("1. Item, nesting level 1",parser2.parseLine("1. Item, nesting level 1"));
    assertEquals("    1. Item, nesting level 3",parser2.parseLine("    1. Item, nesting level 3"));
    assertEquals("      a. Item, nesting level 4",parser2.parseLine("      1. Item, nesting level 4"));
    assertEquals("      b. Item, nesting level 4",parser2.parseLine("      1. Item, nesting level 4"));
    assertEquals("    2. Item, nesting level 3",parser2.parseLine("    1. Item, nesting level 3"));
    assertEquals("2. Item, nesting level 1",parser2.parseLine("1. Item, nesting level 1"));
  }

  @Test
  public void numHeaderToStrHeader() throws Exception {
    //parser.numHeaderToStrHeader(27);
    //System.out.println(parser.numHeaderToStrHeader(27));
    //System.out.println(parser.numHeaderToStrHeader(114));
  }


  @Test
  public void nestingLevel() throws Exception {
    assertEquals(new Integer(1),parser.nestingLevel("1. Item, nesting level 1"));
    assertEquals(new Integer(2),parser.nestingLevel("  1. Item, nesting level 2"));
    assertEquals(new Integer(3),parser.nestingLevel("    1. Item nesting level 3"));
  }


}