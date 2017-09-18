package edu.neu.ccs.cs5004.problem1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by toddl on 4/1/2017.
 * Represents a document processor that reads input, processes it, and writes it to output.
 */
public class DocumentProcessor {
  private BufferedReader documentReader;
  private BufferedWriter documentWriter;

  /**
   * Creates a new document processor with an input and output.
   *
   * @param inputLocation  The file being processed by the word processor.
   * @param outputLocation The output result of the word processor.
   *
   * @throws IOException If files cannot be accessed.
   */
  protected DocumentProcessor(File inputLocation, File outputLocation) throws IOException {
    this.documentReader = createReader(inputLocation);
    this.documentWriter = createWriter(outputLocation);
  }

  /**
   * A constructor used for testing purposes.
   *
   * @param documentReader The buffered reader of input.
   * @param documentWriter The buffered writer of output.
   */
  protected DocumentProcessor(BufferedReader documentReader, BufferedWriter documentWriter) {
    this.documentReader = documentReader;
    this.documentWriter = documentWriter;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    DocumentProcessor that = (DocumentProcessor) object;

    if (!getDocumentReader().equals(that.getDocumentReader())) {
      return false;
    }
    return getDocumentWriter().equals(that.getDocumentWriter());
  }

  @Override
  public int hashCode() {
    int result = getDocumentReader().hashCode();
    result = 31 * result + getDocumentWriter().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "DocumentProcessor{documentReader=" + documentReader
               + ", documentWriter=" + documentWriter + '}';
  }

  /**
   * Gets the input document reader.
   *
   * @return the input document reader.
   */
  protected BufferedReader getDocumentReader() {
    return documentReader;
  }

  /**
   * Sets the input document reader.
   *
   * @param documentReader the input document reader.
   */
  protected void setDocumentReader(BufferedReader documentReader) {
    this.documentReader = documentReader;
  }

  /**
   * Gets the output document writer.
   *
   * @return the output document writer.
   */
  protected BufferedWriter getDocumentWriter() {
    return documentWriter;
  }

  /**
   * Sets the output document writer.
   *
   * @param documentWriter the output document writer.
   */
  protected void setDocumentWriter(BufferedWriter documentWriter) {
    this.documentWriter = documentWriter;
  }

  /**
   * Creates a new buffered reader given an input file.
   *
   * @param filepath The input file being read.
   *
   * @return a new buffered reader given an input file.
   *
   * @throws IOException If the reader cannot be created.
   */
  private BufferedReader createReader(File filepath) throws IOException {
    try {
      return new BufferedReader(
                                   new InputStreamReader(new FileInputStream(filepath),
                                                            "UTF-8"));
    } catch (IOException exception) {
      throw new IOException("The file cannot be opened");
    }
  }

  /**
   * Creates a new buffered writer given an output file.
   *
   * @param filepath The output file being written.
   *
   * @return a new buffered writer given an output file.
   *
   * @throws IOException If the writer cannot be created.
   */
  private BufferedWriter createWriter(File filepath) throws IOException {
    try {
      return new BufferedWriter(
                                   new OutputStreamWriter(new FileOutputStream(filepath),
                                                             "UTF-8"));
    } catch (IOException exception) {
      throw new IOException("The file cannot be opened");
    }
  }

  /**
   * Takes a document state combined with an input file and writes one line to an output file.
   *
   * @param document All data necessary for processing that correlates to a given input.
   *
   * @return a new document state reflecting processor changes.
   *
   * @throws IOException If input or output files cannot be accessed for read or write.
   */
  protected DocumentState processLine(DocumentState document) throws IOException {
    String currentLine = getDocumentReader().readLine();
    Boolean mixedList = document.getPrevLineParser() instanceof ItemizationListParser
                            && document.getCurrEnumListParser().isEligible(currentLine);
    if (currentLine == null) {
      getDocumentReader().close();
      getDocumentWriter().close();
      return null;
    } else if (document.getPrevLineParser() == null) {
      document.setCurrEnumListParser(new EnumerationListParser());
      document.setCurrListValidator(new ListValidator());
      writeLineToOutput(currentLine, document);
    } else if (document.inList() && mixedList) {
      document.getCurrEnumListParser().resetLessSignif((
          (ItemizationListParser)
              document.getPrevLineParser()).nestingLevel(document.getPrevLineWritten()));
      writeLineToOutput(currentLine, document);
    } else {
      writeLineToOutput(currentLine, document);
    }
    return document;
  }

  /**
   * Determines if a new line in a list in a document would comply with the rules of mixed type
   * lists.
   *
   * @param document The current state of the document with regards to processing.
   * @param line     The line being evaluated for compliance in the given list.
   *
   * @return True if the line follows precedent and false otherwise.
   */
  private Boolean validateNewListLine(DocumentState document, String line) {
    if (document.getCurrItemListParser().isEligible(line)) {
      return document.getCurrListValidator().validate(
          ListValidator.ListTypes.ITEMIZATION, document.getCurrItemListParser().nestingLevel(line));
    } else if (document.getCurrEnumListParser().isEligible(line)) {
      return document.getCurrListValidator().validate(ListValidator.ListTypes.ENUMERATION,
          document.getCurrEnumListParser().nestingLevel(line));
    } else {
      return true;
    }
  }

  /**
   * Determines if a line of text is eligible to be parsed by a given parser and, if so, parses
   * the text and writes the output.
   *
   * @param line     The line being evaluated, parsed, and written.
   * @param parser   The parser that is evaluating the line.
   * @param document The current state of the document with regards to processing.
   *
   * @return True if the line was eligible and successfully written, false otherwise.
   *
   * @throws IOException If the output file cannot be written.
   */
  private Boolean writeIfEligible(String line, Parser parser, DocumentState document) throws
      IOException {
    if (parser.isEligible(line)) {
      String writeString = parser.parseLine(line);
      getDocumentWriter().write(writeString);
      getDocumentWriter().newLine();
      document.setPrevLineWritten(writeString);
      document.setPrevLineParser(parser);
      if (document.inList() && !validateNewListLine(document, line)) {
        throw new InvalidListFormatException("One or more lists in the input document are "
                                                 + "incorrectly formatted");
      }
      return true;
    }
    return false;
  }

  /**
   * Evaluates a line of text against all document parsers and writes the parsed result if
   * eligible; otherwise, the line read is written as is.
   *
   * @param line     The line of text being evaluated.
   * @param document The current state of the document with regards to processing.
   */
  private void writeLineToOutput(String line, DocumentState document) throws IOException {
    if (!writeIfEligible(line, document.getCurrHeaderParser(), document)
            && !writeIfEligible(line, document.getCurrEnumListParser(), document)
            && !writeIfEligible(line, document.getCurrItemListParser(), document)) {
      document.setPrevLineParser(null);
      getDocumentWriter().write(line);
      getDocumentWriter().newLine();
    }
  }

  /**
   * Takes all command-line arguments, evaluates them, and uses valid arguments to create a
   * document processor which will process an input file.
   *
   * @param args The command-line arguments supplied by the user.
   *
   * @throws IOException If an error occurs during file access of the input or output files.
   */
  public static void main(String[] args) throws IOException {
    InputValidator commands = new InputValidator(args);
    if (!commands.validate()) {
      throw new InvalidCommandException("Please, ensure that you have 2 parameters: "
                                            + "\n" + "1) InputFile: Filepath for input document "
                                            + "\n" + "2) OutputLoc: Location for "
                                            + "processed document");
    }
    DocumentProcessor processor = new DocumentProcessor(
        commands.getInputFile(), commands.getOutputFile());
    for (DocumentState document = new DocumentState()
         ; document != null
        ; document = processor.processLine(document)) {
      processor.getDocumentWriter().flush();
    }

  }
}
