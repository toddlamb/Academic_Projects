package model;

public class Links {
  int linkId;
  Restaurants restaurant;
  String googleUrl;
  String yelpUrl;
  String restaurantUrl;

  public Links(int linkId, Restaurants restaurant, String googleUrl, String yelpUrl, String restaurantUrl) {
    this.linkId = linkId;
    this.restaurant = restaurant;
    this.googleUrl = googleUrl;
    this.yelpUrl = yelpUrl;
    this.restaurantUrl = restaurantUrl;
  }

  public Links(int linkId) {
    this.linkId = linkId;
  }

  public Links(Restaurants restaurant, String googleUrl, String yelpUrl, String restaurantUrl) {
    this.restaurant = restaurant;
    this.googleUrl = googleUrl;
    this.yelpUrl = yelpUrl;
    this.restaurantUrl = restaurantUrl;
  }

  public int getLinkId() {
    return linkId;
  }

  public void setLinkId(int linkId) {
    this.linkId = linkId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public String getGoogleUrl() {
    return googleUrl;
  }

  public void setGoogleUrl(String googleUrl) {
    this.googleUrl = googleUrl;
  }

  public String getYelpUrl() {
    return yelpUrl;
  }

  public void setYelpUrl(String yelpUrl) {
    this.yelpUrl = yelpUrl;
  }

  public String getRestaurantUrl() {
    return restaurantUrl;
  }

  public void setRestaurantUrl(String restaurantUrl) {
    this.restaurantUrl = restaurantUrl;
  }
}
