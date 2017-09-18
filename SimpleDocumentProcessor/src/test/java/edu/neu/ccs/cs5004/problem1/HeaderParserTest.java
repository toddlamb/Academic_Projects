package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by Todd on 4/1/2017.
 */
public class HeaderParserTest {
  HeaderParser headerParser, headerParser2, headerParser3, firstNull, headerParser4;
  @Before
  public void setUp() throws Exception {
    Pattern pattern = Pattern.compile("^#+");
    Pattern pattern2 = Pattern.compile("test");
    List<Integer> list1 = new ArrayList<Integer>();
    List<Integer> list2 = new LinkedList<Integer>();
    list2.add(5);
    headerParser = new HeaderParser(pattern,list1);
    headerParser2 = new HeaderParser(pattern,list1);
    firstNull = new HeaderParser(null,list1);
    headerParser3 = new HeaderParser(pattern2,list1);
    headerParser4 = new HeaderParser(pattern,list2);
  }

  @Test
  public void parseLine() throws Exception {
    assertEquals("1 Header at Level 1",headerParser.parseLine("# Header at Level 1"));
    assertEquals("1.1 Header at Level 1",headerParser.parseLine("## Header at Level 1"));
    assertEquals("2 Header at Level 1",headerParser.parseLine("# Header at Level 1"));
    assertEquals("2.1.1.1.1 Header at Level 1",headerParser.parseLine("##### Header at Level 1"));
    assertEquals("2.1.2 Header at Level 1",headerParser.parseLine("### Header at Level 1"));
    assertEquals("3 Header at Level 1",headerParser.parseLine("# Header at Level 1"));
  }

  @Test
  public void isEligible() throws Exception {
    assertTrue(headerParser.isEligible("# Header at Level 1"));
    assertFalse(headerParser.isEligible(" # Header at Level 1"));
    assertFalse(headerParser.isEligible(" Header at Level ##"));
  }

  @Test
  public void equals() throws Exception {
    assertEquals(headerParser,headerParser);
    assertNotEquals(headerParser,null);
    assertNotEquals(headerParser,"test");
    assertNotEquals(headerParser,firstNull);
    assertNotEquals(firstNull,headerParser);
    assertNotEquals(headerParser,headerParser3);
    assertNotEquals(headerParser,headerParser4);
    assertEquals(headerParser,headerParser2);
  }

  @Test
  public void hashCodeTest() throws Exception {
    assertEquals(headerParser.hashCode(),headerParser.hashCode());
    assertNotEquals(headerParser.hashCode(),null);
    assertNotEquals(headerParser.hashCode(),"test");
    assertNotEquals(headerParser.hashCode(),firstNull.hashCode());
    assertNotEquals(firstNull.hashCode(),headerParser.hashCode());
    assertNotEquals(headerParser.hashCode(),headerParser3.hashCode());
    assertNotEquals(headerParser.hashCode(),headerParser4.hashCode());
    assertEquals(headerParser.hashCode(),headerParser2.hashCode());
  }

  @Test
  public void toStringTest() throws Exception {
    assertEquals("HeaderParser{} AbstractParserWithList{nestLevelCounter=[]} "
                     + "AbstractParser{parsePattern=^#+}",headerParser.toString());
  }

}