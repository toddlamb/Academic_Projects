package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by toddl on 4/5/2017.
 */
public class DocumentStateTest {
  DocumentState documentState1, documentState2, documentState3;
  @Before
  public void setUp() throws Exception {
    documentState1 = new DocumentState();
    documentState2 = new DocumentState();
    documentState3 = new DocumentState(new HeaderParser(),
                                          new EnumerationListParser(),
                                          new ItemizationListParser(),
                                          new ListValidator(),
                                          new HeaderParser(),
                                          "test");
  }

  @Test
  public void equals() throws Exception {
    assertEquals(documentState1,documentState1);
    assertNotEquals(documentState1,null);
    assertNotEquals(documentState1,"test");
    assertNotEquals(documentState1,documentState2);
    assertNotEquals(documentState1,documentState3);
  }

  @Test
  public void hashCodeTest() throws Exception {
    assertEquals(documentState1.hashCode(),documentState1.hashCode());
    assertNotEquals(documentState1.hashCode(),null);
    assertNotEquals(documentState1.hashCode(),"test");
    assertNotEquals(documentState1.hashCode(),documentState2.hashCode());
    assertNotEquals(documentState1.hashCode(),documentState3.hashCode());
  }

  @Test
  public void toStringTest() throws Exception {
    assertEquals("DocumentState{currHeaderParser=HeaderParser{} "
                     + "AbstractParserWithList{nestLevelCounter=[]} "
                     + "AbstractParser{parsePattern=^#+}, "
                     + "currEnumListParser=EnumerationListParser{} "
                     + "AbstractParserWithList{nestLevelCounter=[]} "
                     + "AbstractParser{parsePattern=^(\\s*)(1\\.|\\.)(?=\\s)}, "
                     + "currItemListParser=ItemizationListParser{} "
                     + "AbstractParser{parsePattern=^\\s*[*+-]}, "
                     + "currListValidator=ListValidator{listTypeMap=[]}, "
                     + "prevLineParser=null, prevLineWritten='null'}",documentState1.toString());
  }

}