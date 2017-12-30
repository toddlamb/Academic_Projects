package model;

public class MainPageData {
  private Integer restaurantId;
  private String restaurantName;
  private boolean hasDelivery;
  private boolean takesReservations;
  private String address;
  private String phoneNumber;
  private String googleUrl;
  private String yelpUrl;
  private Integer priceRange;
  private String cuisineType;
  private double foodSafety;
  private double reviews;

  public MainPageData(Integer restaurantId, String restaurantName, boolean hasDelivery,
      boolean takesReservations, String address, String phoneNumber, String googleUrl,
      String yelpUrl, Integer priceRange, String cuisineType, double foodSafety, double reviews) {
    this.restaurantId = restaurantId;
    this.restaurantName = restaurantName;
    this.hasDelivery = hasDelivery;
    this.takesReservations = takesReservations;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.googleUrl = googleUrl;
    this.yelpUrl = yelpUrl;
    this.priceRange = priceRange;
    this.cuisineType = cuisineType;
    this.foodSafety = foodSafety;
    this.reviews = reviews;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getCuisineType() {
    return cuisineType;
  }

  public void setCuisineType(String cuisineType) {
    this.cuisineType = cuisineType;
  }

  public double getFoodSafety() {
    return foodSafety;
  }

  public void setFoodSafety(double foodSafety) {
    this.foodSafety = foodSafety;
  }

  public double getReviews() {
    return reviews;
  }

  public void setReviews(double reviews) {
    this.reviews = reviews;
  }

  public Integer getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(Integer restaurantId) {
    this.restaurantId = restaurantId;
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

  public Integer getPriceRange() {
    return priceRange;
  }

  public void setPriceRange(Integer priceRange) {
    this.priceRange = priceRange;
  }
}
