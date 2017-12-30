package model;

public class Photos {
  int photoId;
  Restaurants restaurant;
  String content;

  public Photos(int photoId, Restaurants restaurant, String content) {
    this.photoId = photoId;
    this.restaurant = restaurant;
    this.content = content;
  }

  public Photos(int photoId) {
    this.photoId = photoId;
  }

  public Photos(Restaurants restaurant, String content) {
    this.restaurant = restaurant;
    this.content = content;
  }

  public int getPhotoId() {
    return photoId;
  }

  public void setPhotoId(int photoId) {
    this.photoId = photoId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}