package edu.neu.ccs.cs5004.blackbox;

import com.sun.org.apache.xpath.internal.operations.Bool;

import org.junit.Assert;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import edu.neu.ccs.cs5004.problem1.DocumentProcessor;
import edu.neu.ccs.cs5004.problem1.InvalidListFormatException;

/**
 * Created by toddl on 4/3/2017.
 */
public class BlackboxTest {
  String separator = FileSystems.getDefault().getSeparator();
  File file1, file2;

  private static Boolean filesEqual(File file1, File file2) throws IOException {
      BufferedReader file1Reader = new BufferedReader(new FileReader(file1));
      BufferedReader file2Reader = new BufferedReader(new FileReader(file2));
      String file1Line = file1Reader.readLine();
      String file2Line = file2Reader.readLine();
      while (file1Line != null && file2Line != null) {
        if (!file1Line.equals(file2Line)) {
          return false;
        }
        file1Line = file1Reader.readLine();
        file2Line = file2Reader.readLine();
      }
      return true;
  }

  @Test
  public void inputTest() throws Exception {
    DocumentProcessor.main(new String[]{"Testing" + separator + "inputTest.txt","Testing" +
                                                                                    separator +
                                                                                    "outputTest"
                                                                                    + ".txt"});
    file1 = new File("Testing" + separator + "outputTest" + ".txt");
    file2 = new File("ExpectedResults" + separator + "output1.txt");
    Assert.assertTrue(filesEqual(file1,file2));

  }

  @Test
  public void inputTest2() throws Exception {
    DocumentProcessor.main(new String[]{"Testing" + separator + "inputTest2.txt","Testing" +
                                                                                     separator +
                                                                                     "outputTest2.txt"});
    file1 = new File("Testing" + separator + "outputTest2.txt");
    file2 = new File("ExpectedResults" + separator + "output2.txt");
    Assert.assertTrue(filesEqual(file1,file2));

  }

  @Test(expected = InvalidListFormatException.class)
  public void inputTest3() throws Exception {
    DocumentProcessor.main(new String[]{"Testing" + separator + "inputTest3.txt","Testing" +
                                                                                     separator +
                                                                                     "outputTest3.txt"});
  }
}
