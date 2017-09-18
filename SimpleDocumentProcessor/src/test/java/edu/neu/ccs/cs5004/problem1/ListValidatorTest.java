package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by toddl on 4/2/2017.
 */
public class ListValidatorTest {
  ListValidator listValidator, listValidator2, listValidator3;
  @Before
  public void setUp() throws Exception {
    List<ListValidator.ListTypes> list1 = new ArrayList<>();
    List<ListValidator.ListTypes> list2 = new ArrayList<>();
    List<ListValidator.ListTypes> list3 = new ArrayList<>();
    list2.add(ListValidator.ListTypes.ITEMIZATION);
    listValidator = new ListValidator(list1);
    listValidator2 = new ListValidator(list2);
    listValidator3 = new ListValidator(list3);
  }

  @Test
  public void validate() throws Exception {
    assertTrue(listValidator.validate(ListValidator.ListTypes.ITEMIZATION,2));
    assertTrue(listValidator.validate(ListValidator.ListTypes.ENUMERATION,1));
    assertTrue(listValidator.validate(ListValidator.ListTypes.ITEMIZATION,3));
    assertTrue(listValidator.validate(ListValidator.ListTypes.ENUMERATION,1));
    assertFalse(listValidator.validate(ListValidator.ListTypes.ENUMERATION,2));
  }

  @Test
  public void equals() throws Exception {
    assertEquals(listValidator,listValidator);
    assertNotEquals(listValidator,null);
    assertNotEquals(listValidator,"test");
    assertNotEquals(listValidator,listValidator2);
    assertEquals(listValidator,listValidator3);
  }

  @Test
  public void hashCodeTest() throws Exception {
    assertEquals(listValidator.hashCode(),listValidator.hashCode());
    assertNotEquals(listValidator.hashCode(),null);
    assertNotEquals(listValidator.hashCode(),"test");
    assertNotEquals(listValidator.hashCode(),listValidator2.hashCode());
    assertEquals(listValidator.hashCode(),listValidator3.hashCode());
  }

  @Test
  public void toStringTest() throws Exception {
    assertEquals("ListValidator{listTypeMap=[]}",listValidator.toString());
  }

}