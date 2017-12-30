package datamap;

public class LiftInfo {

  private String resortID;
  private int dayNum;
  private String timestamp;
  private String skierID;
  private String liftID;

  public LiftInfo(String resortID, int dayNum, String timestamp, String skierID, String liftID) {
    this.resortID = resortID;
    this.dayNum = dayNum;
    this.timestamp = timestamp;
    this.skierID = skierID;
    this.liftID = liftID;
  }

  public String getResortID() {
    return resortID;
  }

  public int getDayNum() {
    return dayNum;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getSkierID() {
    return skierID;
  }

  public String getLiftID() {
    return liftID;
  }

  @Override
  public String toString() {
    return "datamap.LiftInfo{" +
        "resortID='" + resortID + '\'' +
        ", dayNum=" + dayNum +
        ", timestamp='" + timestamp + '\'' +
        ", skierID='" + skierID + '\'' +
        ", liftID='" + liftID + '\'' +
        '}';
  }
}
