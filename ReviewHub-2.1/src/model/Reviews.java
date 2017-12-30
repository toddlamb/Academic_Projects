package model;

public class Reviews {
  int reviewId;
  Restaurants restaurant;
  int value;
  String content;

  public Reviews(int reviewId, Restaurants restaurant, int value, String content) {
    this.reviewId = reviewId;
    this.restaurant = restaurant;
    this.value = value;
    this.content = content;
  }

  public Reviews(int reviewId) {
    this.reviewId = reviewId;
  }

  public Reviews(Restaurants restaurant, int value, String content) {
    this.restaurant = restaurant;
    this.value = value;
    this.content = content;
  }

  public int getReviewId() {
    return reviewId;
  }

  public void setReviewId(int reviewId) {
    this.reviewId = reviewId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
