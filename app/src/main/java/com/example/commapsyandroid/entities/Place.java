package com.example.commapsyandroid.entities;

import javax.json.JsonObject;

public class Place {

    private Integer ID;

    private Double Latitude;

    private Double Longitude;

    private String Name;

    private String Photo;

    private String Description;

    private String Category;

    public static double toRad(double d)
    {
        return (Math.PI / 180) * d;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public static Place jsonToPlace(JsonObject json)
    {
        try {
            Place place = new Place();

            place.setID(json.getInt("id"));
            place.setLatitude(json.getJsonNumber("latitude").doubleValue());
            place.setLongitude(json.getJsonNumber("longitude").doubleValue());
            place.setName(json.getString("name"));
            place.setPhoto(json.getString("photo"));
            place.setDescription(json.getString("description"));
            place.setCategory(json.getString("category"));

            return place;
        }catch(Exception ex)
        {
            return null;
        }


    }

}
