package model;

public class Cost {
  int costId;
  Restaurants restaurant;
  Integer priceRange;

  public Cost(int costId, Restaurants restaurant, Integer priceRange) {
    this.costId = costId;
    this.restaurant = restaurant;
    this.priceRange = priceRange;
  }

  public Cost(int costId) {
    this.costId = costId;
  }

  public Cost(Restaurants restaurant, Integer priceRange) {
    this.restaurant = restaurant;
    this.priceRange = priceRange;
  }

  public int getCostId() {
    return costId;
  }

  public void setCostId(int costId) {
    this.costId = costId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public Integer getPriceRange() {
    return priceRange;
  }

  public void setPriceRange(Integer priceRange) {
    this.priceRange = priceRange;
  }
}
