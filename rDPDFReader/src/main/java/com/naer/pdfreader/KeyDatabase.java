package com.naer.pdfreader;

public class KeyDatabase {

    public Double Latitude;
    public Double Longitude;
    public String Key;
    public String Place;
    public int Radius = 25;

    public KeyDatabase(Double latitude, Double longitude,int radius, String key,String place) {
        Latitude = latitude;
        Longitude = longitude;
        Radius = radius;
        Key = key;
        Place = place;
    }
}
