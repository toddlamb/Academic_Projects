package datamap;

import com.google.gson.annotations.Expose;

public class IntPair {

  @Expose
  private Integer totalVertical;
  @Expose
  private Integer rideCount;

  public IntPair(Integer totalVertical, Integer rideCount) {
    this.totalVertical = totalVertical;
    this.rideCount = rideCount;
  }

  public Integer getTotalVertical() {
    return totalVertical;
  }

  public Integer getRideCount() {
    return rideCount;
  }

}
