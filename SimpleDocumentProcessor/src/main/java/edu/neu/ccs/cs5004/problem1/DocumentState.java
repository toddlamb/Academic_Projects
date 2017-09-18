package edu.neu.ccs.cs5004.problem1;

/**
 * Represents all data pertaining to a document's status in the word processor.
 * Created by toddl on 4/1/2017.
 */
class DocumentState {
  private HeaderParser currHeaderParser;
  private EnumerationListParser currEnumListParser;
  private ItemizationListParser currItemListParser;
  private ListValidator currListValidator;
  private Parser prevLineParser;
  private String prevLineWritten;

  /**
   * Creates a new document state to be used in a word processor.
   */
  protected DocumentState() {
    this.currHeaderParser = new HeaderParser();
    this.currEnumListParser = new EnumerationListParser();
    this.currItemListParser = new ItemizationListParser();
    this.currListValidator = new ListValidator();
  }

  /**
   * A constructor used for testing purposes only.
   *
   * @param currHeaderParser   The current header parser.
   * @param currEnumListParser The current enumeration list parser.
   * @param currItemListParser The current itemization list parser.
   * @param currListValidator  The current list validator.
   * @param prevLineParser     The previous line parser.
   * @param prevLineWritten    The previous line written.
   */
  public DocumentState(HeaderParser currHeaderParser, EnumerationListParser currEnumListParser,
                       ItemizationListParser currItemListParser, ListValidator currListValidator,
                       Parser prevLineParser, String prevLineWritten) {
    this.currHeaderParser = currHeaderParser;
    this.currEnumListParser = currEnumListParser;
    this.currItemListParser = currItemListParser;
    this.currListValidator = currListValidator;
    this.prevLineParser = prevLineParser;
    this.prevLineWritten = prevLineWritten;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    DocumentState that = (DocumentState) object;

    if (!getCurrHeaderParser().equals(that.getCurrHeaderParser())) {
      return false;
    }
    if (!getCurrEnumListParser().equals(that.getCurrEnumListParser())) {
      return false;
    }
    if (!getCurrItemListParser().equals(that.getCurrItemListParser())) {
      return false;
    }
    if (!getCurrListValidator().equals(that.getCurrListValidator())) {
      return false;
    }
    if (getPrevLineParser() != null ? !getPrevLineParser().equals(that.getPrevLineParser())
            : that.getPrevLineParser() != null) {
      return false;
    }
    return getPrevLineWritten() != null ? getPrevLineWritten().equals(that.getPrevLineWritten())
               : that.getPrevLineWritten() == null;
  }

  @Override
  public int hashCode() {
    int result = getCurrHeaderParser().hashCode();
    result = 31 * result + getCurrEnumListParser().hashCode();
    result = 31 * result + getCurrItemListParser().hashCode();
    result = 31 * result + getCurrListValidator().hashCode();
    result = 31 * result + (getPrevLineParser() != null ? getPrevLineParser().hashCode() : 0);
    result = 31 * result + (getPrevLineWritten() != null ? getPrevLineWritten().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DocumentState{currHeaderParser=" + currHeaderParser + ", currEnumListParser="
               + currEnumListParser + ", currItemListParser=" + currItemListParser
               + ", currListValidator=" + currListValidator + ", prevLineParser="
               + prevLineParser + ", prevLineWritten='" + prevLineWritten + '\'' + '}';
  }

  /**
   * Gets the previous line written by the word processor.
   *
   * @return the previous line written by the word processor.
   */
  protected String getPrevLineWritten() {
    return prevLineWritten;
  }

  /**
   * Sets the previous line written by the word processor.
   *
   * @param prevLineWritten the previous line written by the word processor.
   */
  protected void setPrevLineWritten(String prevLineWritten) {
    this.prevLineWritten = prevLineWritten;
  }

  /**
   * Gets the previous parser used for the previous line.
   *
   * @return The previous parser used for the previous line.
   */
  protected Parser getPrevLineParser() {
    return prevLineParser;
  }

  /**
   * Gets the previous parser used for the previous line.
   *
   * @param prevLineParser the previous parser used for the previous line.
   */
  protected void setPrevLineParser(Parser prevLineParser) {
    this.prevLineParser = prevLineParser;
  }

  /**
   * Gets the current header parser for the word processor.
   *
   * @return the current header parser for the word processor.
   */
  protected HeaderParser getCurrHeaderParser() {
    return currHeaderParser;
  }

  /**
   * Gets the current enumeration list parser for the word processor.
   *
   * @return the current enumeration list parser for the word processor.
   */
  protected EnumerationListParser getCurrEnumListParser() {
    return currEnumListParser;
  }

  /**
   * Gets the current itemization list parser for the word processor.
   *
   * @return the current itemization list parser for the word processor.
   */
  protected ItemizationListParser getCurrItemListParser() {
    return currItemListParser;
  }

  /**
   * Sets the current itemization list parser for the word processor.
   *
   * @param currEnumListParser the current itemization list parser for the word processor.
   */
  protected void setCurrEnumListParser(EnumerationListParser currEnumListParser) {
    this.currEnumListParser = currEnumListParser;
  }

  /**
   * Gets the current list validator for the word processor.
   *
   * @return the current list validator for the word processor.
   */
  protected ListValidator getCurrListValidator() {
    return currListValidator;
  }

  /**
   * Sets the current list validator for the word processor.
   *
   * @param currListValidator the current list validator for the word processor.
   */
  protected void setCurrListValidator(ListValidator currListValidator) {
    this.currListValidator = currListValidator;
  }

  /**
   * Determines if the processor is in the middle of processing a list contained in the document.
   *
   * @return True if the previous line parser is a list parser and false otherwise.
   */
  protected Boolean inList() {
    return prevLineParser instanceof EnumerationListParser
               || prevLineParser instanceof ItemizationListParser;
  }
}
