package model;

public class Cuisine {
  int cuisineId;
  Restaurants restaurant;
  String cuisineType;

  public Cuisine(int cuisineId, Restaurants restaurant, String cuisineType) {
    this.cuisineId = cuisineId;
    this.restaurant = restaurant;
    this.cuisineType = cuisineType;
  }

  public Cuisine(int cuisineId) {
    this.cuisineId = cuisineId;
  }

  public Cuisine(Restaurants restaurant, String cuisineType) {
    this.restaurant = restaurant;
    this.cuisineType = cuisineType;
  }

  public int getCuisineId() {
    return cuisineId;
  }

  public void setCuisineId(int cuisineId) {
    this.cuisineId = cuisineId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public String getCuisineType() {
    return cuisineType;
  }

  public void setCuisineType(String cuisineType) {
    this.cuisineType = cuisineType;
  }

}
