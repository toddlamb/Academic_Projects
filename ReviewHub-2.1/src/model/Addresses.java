package model;

public class Addresses {
  int addressId;
  Restaurants restaurant;
  String formattedAddress;
  double latitude;
  double longitude;
  String streetOne;
  String streetTwo;
  String city;
  String state;
  String country;
  String zipCode;
  String formattedPhoneNumber;
  String internationalPhoneNumber;

  public Addresses(int addressId, Restaurants restaurant, String formattedAddress, double latitude,
                   double longitude, String streetOne, String streetTwo, String city, String state,
                   String country, String zipCode, String formattedPhoneNumber,
                   String internationalPhoneNumber) {
    this.addressId = addressId;
    this.restaurant = restaurant;
    this.formattedAddress = formattedAddress;
    this.latitude = latitude;
    this.longitude = longitude;
    this.streetOne = streetOne;
    this.streetTwo = streetTwo;
    this.city = city;
    this.state = state;
    this.country = country;
    this.zipCode = zipCode;
    this.formattedPhoneNumber = formattedPhoneNumber;
    this.internationalPhoneNumber = internationalPhoneNumber;
  }

  public Addresses(Restaurants restaurant, String formattedAddress, double latitude,
                   double longitude, String streetOne, String streetTwo, String city,
                   String state, String country, String zipCode, String formattedPhoneNumber,
                   String internationalPhoneNumber) {
    this.restaurant = restaurant;
    this.formattedAddress = formattedAddress;
    this.latitude = latitude;
    this.longitude = longitude;
    this.streetOne = streetOne;
    this.streetTwo = streetTwo;
    this.city = city;
    this.state = state;
    this.country = country;
    this.zipCode = zipCode;
    this.formattedPhoneNumber = formattedPhoneNumber;
    this.internationalPhoneNumber = internationalPhoneNumber;
  }

  public Addresses(int addressId) {
    this.addressId = addressId;
  }

  public int getAddressId() {
    return addressId;
  }

  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }

  public Restaurants getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurants restaurant) {
    this.restaurant = restaurant;
  }

  public String getFormattedAddress() {
    return formattedAddress;
  }

  public void setFormattedAddress(String formattedAddress) {
    this.formattedAddress = formattedAddress;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getStreetOne() {
    return streetOne;
  }

  public void setStreetOne(String streetOne) {
    this.streetOne = streetOne;
  }

  public String getStreetTwo() {
    return streetTwo;
  }

  public void setStreetTwo(String streetTwo) {
    this.streetTwo = streetTwo;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getFormattedPhoneNumber() {
    return formattedPhoneNumber;
  }

  public void setFormattedPhoneNumber(String formattedPhoneNumber) {
    this.formattedPhoneNumber = formattedPhoneNumber;
  }

  public String getInternationalPhoneNumber() {
    return internationalPhoneNumber;
  }

  public void setInternationalPhoneNumber(String internationalPhoneNumber) {
    this.internationalPhoneNumber = internationalPhoneNumber;
  }
}
