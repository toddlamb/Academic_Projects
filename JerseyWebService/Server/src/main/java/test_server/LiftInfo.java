package test_server;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents RFID data sent when a skier uses a lift.
 */
class LiftInfo {

  private String resortID;
  private Integer dayNum;
  private String time;
  private String skierID;
  private String liftID;

  /**
   * @param resortID Identifier of resort.
   * @param dayNum Day number.
   * @param time Timestamp lift ride occured.
   * @param skierID Identifier of skier.
   * @param liftID Identifier of lift.
   */
  public LiftInfo(String resortID, Integer dayNum, String time, String skierID, String liftID) {
    this.resortID = resortID;
    this.dayNum = dayNum;
    this.time = time;
    this.skierID = skierID;
    this.liftID = liftID;
  }

  /**
   * Determines if lift info is valid.
   *
   * @return true if lift info is valid for database insertion and false otherwise.
   */
  public boolean isValid() {
    return resortID != null
        && dayNum != null
        && time != null
        && skierID != null
        && liftID != null
        && StringUtils.isNumeric(time)
        && (Integer.parseInt(time) >= 0 && Integer.parseInt(time) <= 360)
        && StringUtils.isNumeric(skierID)
        && StringUtils.isNumeric(liftID)
        && (Integer.parseInt(liftID) > 0 && Integer.parseInt(liftID) < 41);
  }

  public String getResortID() {
    return resortID;
  }

  public int getDayNum() {
    return dayNum;
  }

  public String getSkierID() {
    return skierID;
  }

  public String getLiftID() {
    return liftID;
  }

  @Override
  public String toString() {
    return "LiftInfo{" +
        "resortID='" + resortID + '\'' +
        ", dayNum=" + dayNum +
        ", timestamp='" + time + '\'' +
        ", skierID='" + skierID + '\'' +
        ", liftID='" + liftID + '\'' +
        '}';
  }

  public String getTime() {
    return time;
  }
}
