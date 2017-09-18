package edu.neu.ccs.cs5004.problem1;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

/**
 * Created by toddl on 4/5/2017.
 */
public class DocumentProcessorTest {
  String separator = FileSystems.getDefault().getSeparator();
  BufferedReader reader1;
  BufferedWriter writer1;
  DocumentProcessor documentProcessor, documentProcessor2;

  @Before
  public void setUp() throws Exception {
    reader1 = new BufferedReader(
                                    new FileReader(new File("Testing" + separator + "inputTest.txt")));
    writer1 = new BufferedWriter(new FileWriter(new File("Testing" + separator + "outputTest"
                                                             + ".txt")));
    documentProcessor = new DocumentProcessor(reader1, writer1);
    documentProcessor2 = new DocumentProcessor(reader1, writer1);
  }

  @Test
  public void equals() throws Exception {
    assertEquals(documentProcessor, documentProcessor);
    assertNotEquals(documentProcessor, null);
    assertNotEquals(documentProcessor, "string");
    assertNotEquals(documentProcessor,
        new DocumentProcessor(new File("Testing" + separator + "inputTest.txt"),
                                 new File("Testing" + separator + "outputTest.txt")));
    assertEquals(documentProcessor,documentProcessor2);
  }

  @Test
  public void hashCodeTest() throws Exception {
    assertEquals(documentProcessor.hashCode(), documentProcessor.hashCode());
    assertNotEquals(documentProcessor.hashCode(), null);
    assertNotEquals(documentProcessor.hashCode(), "string");
    assertNotEquals(documentProcessor.hashCode(),
        new DocumentProcessor(new File("Testing" + separator + "inputTest.txt"),
                                 new File("Testing" + separator
                                              + "outputTest.txt")).hashCode());
    assertEquals(documentProcessor.hashCode(),documentProcessor2.hashCode());
  }



}

