package model;

public class Favorites {
  int favoriteId;
  Restaurants restaurant;
  Users user;

  public Favorites(int favoriteId, Restaurants restaurant, Users user) {
    this.favoriteId = favoriteId;
    this.restaurant = restaurant;
    this.user = user;
  }

  public Favorites(int favoriteId) {
    this.favoriteId = favoriteId;
  }

  public Favorites(Restaurants restaurant, Users user) {
    this.restaurant = restaurant;
    this.user = user;
  }

  public int getFavoriteId() {
    return favoriteId;
  }

  public void setFavoriteId(int favoriteId) {
    this.favoriteId = favoriteId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }
}
