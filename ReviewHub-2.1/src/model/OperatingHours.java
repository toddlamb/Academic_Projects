package model;

import java.util.Date;

public class OperatingHours {
  int operatingHourId;
  Restaurants restaurant;
  DayOfWeek day;
  Date startTime;
  Date endTime;

  public enum DayOfWeek {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
  }

  public OperatingHours(int operatingHourId, Restaurants restaurant, DayOfWeek day, Date startTime, Date endTime) {
    this.operatingHourId = operatingHourId;
    this.restaurant = restaurant;
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public OperatingHours(int operatingHourId) {
    this.operatingHourId = operatingHourId;
  }

  public OperatingHours(Restaurants restaurant, DayOfWeek day, Date startTime, Date endTime) {
    this.restaurant = restaurant;
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public int getOperatingHourId() {
    return operatingHourId;
  }

  public void setOperatingHourId(int operatingHourId) {
    this.operatingHourId = operatingHourId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public DayOfWeek getDay() {
    return day;
  }

  public void setDay(DayOfWeek day) {
    this.day = day;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
}
