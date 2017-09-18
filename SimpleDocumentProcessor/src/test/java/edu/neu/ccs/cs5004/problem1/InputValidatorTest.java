package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

/**
 * Created by Todd on 4/4/2017.
 */
public class InputValidatorTest {
  String separator = FileSystems.getDefault().getSeparator();
  File file1, file2, file3, file4;
  InputValidator inputValidator, inputValidator2, inputValidator3, inputValidator4;
  @Before
  public void setUp() throws Exception {
    file1 = new File("Testing" + separator + "inputTest.txt");
    file2 = new File("Testing" + separator + "inputTest.txt");
    file3 = new File("Testing" + separator + "inputTest2.txt");
    inputValidator = new InputValidator(file1,file1);
    inputValidator2 = new InputValidator(file3,file1);
    inputValidator3 = new InputValidator(file1,file3);
    inputValidator4 = new InputValidator(file1,file1);
  }

  @Test
  public void equals() throws Exception {
    assertEquals(inputValidator,inputValidator);
    assertNotEquals(inputValidator,null);
    assertNotEquals(inputValidator,"test");
    assertNotEquals(inputValidator,inputValidator2);
    assertNotEquals(inputValidator,inputValidator3);
    assertEquals(inputValidator,inputValidator4);
  }

  @Test
  public void hashCodeTest() throws Exception {
    assertEquals(inputValidator.hashCode(),inputValidator.hashCode());
    assertNotEquals(inputValidator.hashCode(),null);
    assertNotEquals(inputValidator.hashCode(),"test");
    assertNotEquals(inputValidator.hashCode(),inputValidator2.hashCode());
    assertNotEquals(inputValidator.hashCode(),inputValidator3.hashCode());
    assertEquals(inputValidator.hashCode(),inputValidator4.hashCode());
  }

  @Test
  public void toStringTest() throws Exception {
    assertEquals("InputValidator{inputFile=Testing\\inputTest.txt, "
                     + "outputFile=Testing\\inputTest"
                     + ".txt}",inputValidator.toString());
  }

  @Test(expected = InvalidCommandException.class)
  public void throwsInConstruc() throws Exception {
    InputValidator inputValidator = new InputValidator(new String[] {"this", "is", "a"});
  }

  @Test
  public void validate() throws Exception {
    assertFalse(new InputValidator(new String[] {"test","this"}).validate());
  }



}