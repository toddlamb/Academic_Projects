package utilities;

import com.google.common.net.InetAddresses;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Parses and validates command line arguments
 */
public class CommandValidator {

  private static final String THREAD_FLAG = "-t";
  private static final String METHOD_FLAG = "-m";
  private static final String PORT_FLAG = "-p";
  private static final int MAX_ARGS = 7;
  private static final int PARAM_PER_ARG = 2;
  private static final int REQUIRED_ARG = 1;
  public static final int MAX_NUMERIC_ARGUMENTS = 3;
  private Integer threadCount;
  private Integer method;
  private Integer port;
  private String ipAddress;
  private boolean isValid;

  public enum Method {POST, GET, SEQUENTIAL};

  /**
   * Parses and validates command line arguments.
   *
   * @param args Command line arguments
   */
  public CommandValidator(String[] args) {
    final Integer LAST_INDEX = args.length - 1;
    this.isValid = true;
    //Validate allowable number of arguments
    if (args.length > MAX_ARGS) {
      this.isValid = false;
    }
    int num_count = 0;
    //Validate all strings are valid flags or valid ip
    num_count = get_valid_arg_count(args, num_count);
    //Valid count of numerical arguments
    this.isValid = argument_count_is_valid(num_count);
    //Validate correct order of parameters
    //Last argument must be ip address
    if (!valid_location(args)) {
      this.isValid = false;
    } else {
      this.ipAddress = args[args.length - 1];
    }
    int flag_count = set_and_count_optional_arguments(args);
    validate_arg_count_given_flag_count(args, flag_count);
    if (this.method == null) {
      this.method = Method.POST.ordinal();
    }
  }

  /**
   * Sets valid field to false if number of valid flags aren't proportional to valid arg values
   *
   * @param args Command line args
   * @param flag_count Number of optional paramater flags in args
   */
  private void validate_arg_count_given_flag_count(String[] args, int flag_count) {
    if (args.length != flag_count * PARAM_PER_ARG + REQUIRED_ARG) {
      this.isValid = false;
    }
  }

  //

  /**
   * Counts number of optional arguments supplied and sets their values in the object
   *
   * @param args Command line args.
   * @return number of optional arguments supplied
   */
  private int set_and_count_optional_arguments(String[] args) {
    int flag_count = 0;
    int last_index = args.length - 1;
    for (int i = 0; i < args.length; i++) {
      //Optional parameters must be in order
      if (is_optional_flag(args[i])) {
        // Checks that any argument values other than ip address are numerical
        switch (args[i]) {
          //Sets object fields if they exist and updates count
          case THREAD_FLAG:
            this.threadCount = Integer.parseInt(args[i + 1]);
            flag_count++;
            break;
          case METHOD_FLAG:
            this.method = Integer.parseInt(args[i + 1]);
            flag_count++;
            break;
          case PORT_FLAG:
            this.port = Integer.parseInt(args[i + 1]);
            flag_count++;
            break;
        }
        if (i == last_index || !StringUtils.isNumeric(args[i + 1])) {
          this.isValid = false;
        }
      }
    }
    return flag_count;
  }

  /**
   * Returns true if the string supplied is a valid optional argument flag and false otherwise
   *
   * @param argument Command line parameter
   * @return true if the string supplied is a valid optional argument flag and false otherwise
   */
  private static boolean is_optional_flag(String argument) {
    return argument.equals(THREAD_FLAG) || argument.equals(METHOD_FLAG) || argument
        .equals(PORT_FLAG);
  }


  /**
   * Returns true if last argument is valid ip address or url and false otherwise
   *
   * @param args Command line parameter
   * @return true if last argument is valid ip address or url and false otherwise
   */
  private boolean valid_location(String[] args) {
    String[] schemes = {"http"};
    UrlValidator urlValidator = new UrlValidator(schemes);
    return InetAddresses.isInetAddress(args[args.length - 1]) || urlValidator
        .isValid(args[args.length - 1]);
  }


  /**
   * Validates maximum number of arguments
   *
   * @param num_count Number of parameter supplied
   * @return true if number of arguments is valid and false otherwise.
   */
  private boolean argument_count_is_valid(int num_count) {
    return num_count <= MAX_NUMERIC_ARGUMENTS;
  }

  //

  /**
   * Counts the number of arguments supplied that are valid
   *
   * @param args Command line arguments
   * @param num_count counter
   * @return the number of arguments supplied that are valid
   */
  private int get_valid_arg_count(String[] args, int num_count) {
    for (String string : args) {
      if (StringUtils.isNumeric(string)) {
        num_count++;
      } else if (!(is_optional_flag(string))) {
        this.isValid = false;
      }
    }
    return num_count;
  }


  public Integer getThreadCount() {
    return threadCount;
  }

  public Integer getMethod() {
    return method;
  }

  public Integer getPort() {
    return port;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public boolean isValid() {
    return isValid;
  }
}
