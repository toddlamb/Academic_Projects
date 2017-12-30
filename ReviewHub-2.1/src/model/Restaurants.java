package model;


public class Restaurants {
  int RestaurantId;
  String Name;
  boolean isOpen;
  boolean hasDelivery;
  boolean takesReservations;

  public Restaurants(int restaurantId, String name, boolean isOpen, boolean hasDelivery, boolean takesReservations) {
    RestaurantId = restaurantId;
    Name = name;
    this.isOpen = isOpen;
    this.hasDelivery = hasDelivery;
    this.takesReservations = takesReservations;
  }

  public Restaurants(int restaurantId) {
    RestaurantId = restaurantId;
  }

  public Restaurants(String name, boolean isOpen, boolean hasDelivery, boolean takesReservations) {
    Name = name;
    this.isOpen = isOpen;
    this.hasDelivery = hasDelivery;
    this.takesReservations = takesReservations;
  }

  public int getRestaurantId() {
    return RestaurantId;
  }

  public void setRestaurantId(int restaurantId) {
    RestaurantId = restaurantId;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public boolean getisOpen() {
    return isOpen;
  }

  public void setOpen(boolean open) {
    isOpen = open;
  }

  public boolean isHasDelivery() {
    return hasDelivery;
  }

  public void setHasDelivery(boolean hasDelivery) {
    this.hasDelivery = hasDelivery;
  }

  public boolean isTakesReservations() {
    return takesReservations;
  }

  public void setTakesReservations(boolean takesReservations) {
    this.takesReservations = takesReservations;
  }
}
