package model;


import java.util.Date;

public class FoodSafety {
  int foodSafetyId;
  Restaurants restaurant;
  int inspectionScore;
  String inspectionResult;
  Date inspectionDate;

  public FoodSafety(int foodSafetyId, Restaurants restaurant, Integer inspectionScore,
                    String inspectionResult, Date inspectionDate) {
    this.foodSafetyId = foodSafetyId;
    this.restaurant = restaurant;
    this.inspectionScore = inspectionScore;
    this.inspectionResult = inspectionResult;
    this.inspectionDate = inspectionDate;
  }

  public FoodSafety(Restaurants restaurant, Integer inspectionScore, String inspectionResult,
                    Date inspectionDate) {
    this.restaurant = restaurant;
    this.inspectionScore = inspectionScore;
    this.inspectionResult = inspectionResult;
    this.inspectionDate = inspectionDate;
  }

  public FoodSafety(int foodSafetyId) {
    this.foodSafetyId = foodSafetyId;
  }

  public int getFoodSafetyId() {
    return foodSafetyId;
  }

  public void setFoodSafetyId(int foodSafetyId) {
    this.foodSafetyId = foodSafetyId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public Integer getInspectionScore() {
    return inspectionScore;
  }

  public void setInspectionScore(int inspectionScore) {
    this.inspectionScore = inspectionScore;
  }

  public String getInspectionResult() {
    return inspectionResult;
  }

  public void setInspectionResult(String inspectionResult) {
    this.inspectionResult = inspectionResult;
  }

  public Date getInspectionDate() {
    return inspectionDate;
  }

  public void setInspectionDate(Date inspectionDate) {
    this.inspectionDate = inspectionDate;
  }
}