package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by Todd on 4/1/2017.
 */
public class ItemizationListParserTest {
  ItemizationListParser parser, parser2, parser3, nullparser, nullparser2;
  @Before
  public void setUp() throws Exception {
    Pattern pattern = Pattern.compile("^\\s*[*+-]");
    parser = new ItemizationListParser(pattern);
    parser3 = new ItemizationListParser(pattern);
    parser2 = new ItemizationListParser(Pattern.compile("test"));
    nullparser = new ItemizationListParser(null);
    nullparser2 = new ItemizationListParser(null);
  }

  @Test
  public void equals() throws Exception {
    assertEquals(parser,parser);
    assertNotEquals(parser,null);
    assertNotEquals(parser,"test");
    assertEquals(nullparser,nullparser2);
    assertNotEquals(nullparser,parser);
    assertNotEquals(parser,nullparser);
    assertNotEquals(parser2,parser3);
    assertEquals(parser,parser3);
  }

  @Test
  public void hashCodeTest() throws Exception {
    assertEquals(parser.hashCode(),parser.hashCode());
    assertNotEquals(parser.hashCode(),null);
    assertNotEquals(parser.hashCode(),"test");
    assertEquals(nullparser.hashCode(),nullparser2.hashCode());
    assertNotEquals(nullparser.hashCode(),parser.hashCode());
    assertNotEquals(parser.hashCode(),nullparser.hashCode());
    assertNotEquals(parser2.hashCode(),parser3.hashCode());
    assertEquals(parser.hashCode(),parser3.hashCode());
  }

  @Test
  public void toStringTest() throws Exception {
    assertEquals("ItemizationListParser{} AbstractParser{parsePattern=^\\s*[*+-]}",
        parser.toString());
  }

  @Test
  public void parseLine() throws Exception {
    assertEquals("* Item",parser.parseLine("* Item"));
    assertEquals("  * Item",parser.parseLine("  - Item"));
    assertEquals("    * Item",parser.parseLine("    + Item"));
    assertEquals("  * Item",parser.parseLine("  - Item"));

  }

  @Test
  public void isEligible() throws Exception {
    assertTrue(parser.isEligible("* Item this is a test"));
    assertTrue(parser.isEligible("      * This is another test"));
    assertTrue(parser.isEligible("  + One more test"));
    assertTrue(parser.isEligible("  - Another"));
    assertFalse(parser.isEligible("  ## Testing again and again"));
    assertFalse(parser.isEligible("Here's a final test * + -"));
  }

  @Test
  public void nestingLevel() throws Exception {
    assertEquals(new Integer(1),parser.nestingLevel("* Item this is a test"));
    assertEquals(new Integer(2),parser.nestingLevel("  * Item this is a test"));
    assertEquals(new Integer(4),parser.nestingLevel("      * Item this  is a test"));
  }

}