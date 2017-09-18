package edu.neu.ccs.cs5004.problem1;

import java.io.File;

/**
 * Processes and validates input supplied by the user.
 * Created by toddl on 4/3/2017.
 */
public class InputValidator {
  private File inputFile;
  private File outputFile;

  /**
   * Creates a new input validator from command line arguments.
   *
   * @param cmdLineArgs The command line arguments supplied by the user.
   *
   * @throws InvalidCommandException If the user specifies more or less than two commands.
   */
  public InputValidator(String[] cmdLineArgs) {
    if (cmdLineArgs.length != 2) {
      throw new InvalidCommandException("Please, ensure that you have 2 and only 2 parameters: "
                                            + "\n" + "1) InputFile: Filepath for input document "
                                            + "\n" + "2) OutputLoc: Location for "
                                            + "processed document");
    }
    this.inputFile = new File(cmdLineArgs[0]);
    this.outputFile = new File(cmdLineArgs[1]);
  }

  /**
   * A constructor used for testing purposes.
   *
   * @param inputFile  The input file use for reading input.
   * @param outputFile The output file used for writing output.
   */
  public InputValidator(File inputFile, File outputFile) {
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    InputValidator that = (InputValidator) object;

    if (!getInputFile().equals(that.getInputFile())) {
      return false;
    }
    return getOutputFile().equals(that.getOutputFile());
  }

  @Override
  public int hashCode() {
    int result = getInputFile().hashCode();
    result = 31 * result + getOutputFile().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "InputValidator{inputFile=" + inputFile + ", outputFile=" + outputFile + '}';
  }

  /**
   * Determines if the input and output arguments given by the user can be used in the word
   *     processor.
   *
   * @return True if the input and output arguments given by the user can be used in the word
   *     processor and false otherwise.
   */
  public Boolean validate() {
    return getInputFile().canRead()
               && getInputFile().exists()
               && (getOutputFile().canWrite() || getOutputFile().getParentFile().isDirectory());
  }

  /**
   * Gets the input file specified by the user.
   *
   * @return the input file specified by the user.
   */
  public File getInputFile() {
    return inputFile;
  }

  /**
   * Gets the output file specified by the user.
   *
   * @return the output file specified by the user.
   */
  public File getOutputFile() {
    return outputFile;
  }
}


